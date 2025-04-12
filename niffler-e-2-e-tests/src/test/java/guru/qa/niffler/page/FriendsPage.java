package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {

  private final ElementsCollection friendsTableRows = $$("#friends tr");
  private final ElementsCollection requestsTableRows = $$("#requests tr");
  private final ElementsCollection allPeopleTableRows = $$("#all tr");
  private final SelenideElement allPeopleTab = $("a[href='/people/all']");
  private final SelenideElement noUsersAlert = $x(".//p[contains(text(),'There are no users yet')]");

  public FriendsPage goToAllPeopleTab() {
    allPeopleTab.click();
    return this;
  }

  public void checkThatFriendsTableContains(String friend) {
    friendsTableRows.find(text(friend))
            .shouldBe(visible);
  }

  public void checkThatFriendsTableIsEmpty() {
    noUsersAlert.shouldBe(visible);
  }

  public void checkThatIncomeInvitationFromUserPresent(String username) {
    requestsTableRows.find(text(username))
            .shouldBe(visible);
  }

  public void checkThatOutcomeInvitationForUserPresent(String username) {
    allPeopleTableRows.find(text(username)).$$("td").last().shouldHave(exactText("Waiting..."));
    }
}
