package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class MenuComponent {

  private final SelenideElement profileBtn = $("a[href='/profile']");
  private final SelenideElement friendsBtn = $("a[href='/people/friends']");

  public ProfilePage goToProfilePage() {
    profileBtn.click();
    return new ProfilePage();
  }

  public FriendsPage goToFriendsPage() {
    friendsBtn.click();
    return new FriendsPage();
  }
}
