package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterResultPage {

    private final SelenideElement signInBtn =  $(".form_sign-in");
    private final SelenideElement congratsText = $(".form__paragraph");

    public LoginPage doSignIn() {
        signInBtn.click();
        return new LoginPage();
    }

    public void checkThatCongratsTextIsVisible() {
        congratsText.should(visible);
    }
}
