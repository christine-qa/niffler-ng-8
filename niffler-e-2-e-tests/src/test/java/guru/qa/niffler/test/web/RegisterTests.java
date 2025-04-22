package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import guru.qa.niffler.page.RegisterResultPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class RegisterTests {

    private static final Config CFG = Config.getInstance();
    private static final String password = "12345";

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUsername(RandomDataUtils.randomUsername())
                .setPassword(password)
                .setPasswordSubmit(password)
                .doSignUp();

        new RegisterResultPage().checkThatCongratsTextIsVisible();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String username = RandomDataUtils.randomUsername();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .doSignUp()
                .doSignIn()
                .createAccount()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .doSignUp();

        new RegisterPage().checkThatErrorContains(String.format("Username `%s` already exists", username));
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUsername("snake")
                .setPassword(password)
                .setPasswordSubmit("12346")
                .doSignUp();

        new RegisterPage().checkThatErrorContains("Passwords should be equal");
    }
}
