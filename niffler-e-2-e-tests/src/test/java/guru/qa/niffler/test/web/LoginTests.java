package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

@WebTest
public class LoginTests {

  private static final Config CFG = Config.getInstance();

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .doLogin("duck", "12345");

    MainPage mainPage = new MainPage();
    mainPage.checkThatStatBlockIsVisible();
    mainPage.checkThatSpendingBlockIsVisible();
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .doLogin("notExistingUser", "12345");

    new LoginPage().checkThatErrorContains("Неверные учетные данные пользователя");
  }
}
