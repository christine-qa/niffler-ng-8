package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import guru.qa.niffler.page.RegisterResultPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

@ExtendWith(BrowserExtension.class)
public class RegisterTests {

    private static final Config CFG = Config.getInstance();
    private static final String password = "12345";

    @Test
    void shouldRegisterNewUser() {
        String username = UUID.randomUUID().toString();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .doSignUp();

        new RegisterResultPage().checkThatCongratsTextIsVisible();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String username = UUID.randomUUID().toString();

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
