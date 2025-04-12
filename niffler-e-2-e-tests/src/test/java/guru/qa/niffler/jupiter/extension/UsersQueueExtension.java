package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.UserType;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  public record StaticUser(String username, String password, String friend, String income, String outcome) {
  }

  private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> USERS_WITH_FRIENDS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> USERS_WITH_INCOME_REQUEST = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> USERS_WITH_OUTCOME_REQUEST = new ConcurrentLinkedQueue<>();


  static {
    EMPTY_USERS.add(new StaticUser("bee", "12345", null, null, null));
    USERS_WITH_OUTCOME_REQUEST.add(new StaticUser("cat", "12345", "duck", null, "flower"));
    USERS_WITH_INCOME_REQUEST.add(new StaticUser("flower", "12345", "duck", "cat", null));
    USERS_WITH_FRIENDS.add(new StaticUser("duck", "12345", "flower", null, null));
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
      Arrays.stream(context.getRequiredTestMethod().getParameters())
        .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
        .findFirst()
        .map(p -> p.getAnnotation(UserType.class)) // находим все паараметры
        .ifPresent(ut -> {
          Optional<StaticUser> user = Optional.empty(); // создаем переменную для хранения пользователя
          StopWatch sw = StopWatch.createStarted();
          while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
              switch (ut.type()) {
                  case EMPTY -> user = Optional.ofNullable(EMPTY_USERS.poll());
                  case WITH_FRIENDS -> user = Optional.ofNullable(USERS_WITH_FRIENDS.poll());
                  case WITH_INCOME_REQUEST -> user = Optional.ofNullable(USERS_WITH_INCOME_REQUEST.poll());
                  case WITH_OUTCOME_REQUEST -> user = Optional.ofNullable(USERS_WITH_OUTCOME_REQUEST.poll());
              }
          }
          Allure.getLifecycle().updateTestCase(testCase ->
              testCase.setStart(new Date().getTime())
          );
          user.ifPresentOrElse(
                  u -> ((Map<UserType, StaticUser>) context.getStore(NAMESPACE).getOrComputeIfAbsent(
                          context.getUniqueId(),
                          key -> new HashMap()
                  )).put(ut, u),
                () -> {
                throw new IllegalStateException("Can`t obtain user after 30s.");
              }
          );
        });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
      Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
        context.getUniqueId(),
        Map.class
    );

      for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
          switch (e.getKey().type()) {
              case WITH_FRIENDS -> USERS_WITH_FRIENDS.add(e.getValue());
              case EMPTY -> EMPTY_USERS.add(e.getValue());
              case WITH_INCOME_REQUEST -> USERS_WITH_INCOME_REQUEST.add(e.getValue());
              case WITH_OUTCOME_REQUEST -> USERS_WITH_OUTCOME_REQUEST.add(e.getValue());
          }
      }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
      return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                              &&
                              AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
  }

  @Override
  public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return (StaticUser) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class)
            .get(AnnotationSupport.findAnnotation(parameterContext.getParameter(), UserType.class).get());
  }
}
