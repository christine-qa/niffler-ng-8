package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MenuPage {

  private final SelenideElement profileBtn = $("a[href='/profile']");

  public ProfilePage goToProfile() {
    profileBtn.click();
    return new ProfilePage();
  }
}
