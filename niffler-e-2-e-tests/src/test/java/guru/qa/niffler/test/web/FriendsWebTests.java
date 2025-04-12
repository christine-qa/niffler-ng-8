package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.annotation.UserType.Type.WITH_INCOME_REQUEST;
import static guru.qa.niffler.jupiter.annotation.UserType.Type.WITH_FRIENDS;
import static guru.qa.niffler.jupiter.annotation.UserType.Type.WITH_OUTCOME_REQUEST;

@ExtendWith({UsersQueueExtension.class, BrowserExtension.class})
public class FriendsWebTests {

    private static final Config CFG = Config.getInstance();

    @Test
    void friendShouldBePresentInTheFriendsTable(@UserType(type = WITH_FRIENDS) StaticUser user) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.password())
                .goToMenu()
                .goToFriendsPage()
                .checkThatFriendsTableContains(user.friend());
    }

    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserType(type = EMPTY) StaticUser user) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.password())
                .goToMenu()
                .goToFriendsPage()
                .checkThatFriendsTableIsEmpty();
    }

    @Test
    void incomeInvitationShouldBePresentInTheFriendsTable(@UserType(type = WITH_INCOME_REQUEST) StaticUser user) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.password())
                .goToMenu()
                .goToFriendsPage()
                .checkThatIncomeInvitationFromUserPresent(user.income());
    }

    @Test
    void outcomeInvitationShouldBePresentInTheFriendsTable(@UserType(type = WITH_OUTCOME_REQUEST) StaticUser user) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin(user.username(), user.password())
                .goToMenu()
                .goToFriendsPage()
                .goToAllPeopleTab()
                .checkThatOutcomeInvitationForUserPresent(user.outcome());
    }
}
