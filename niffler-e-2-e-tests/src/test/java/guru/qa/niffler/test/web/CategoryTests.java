package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

@WebTest
public class CategoryTests {

    private static final Config CFG = Config.getInstance();

    @User(username = "duck", categories = @Category(archived = true))
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin("duck", "12345")
                .goToMenu()
                .goToProfilePage()
                .showArchivedCategories();

        ProfilePage profilePage = new ProfilePage();
        profilePage.checkThatCategoriesListContains(categoryJson.name());
    }

    @User(username = "duck", categories = @Category(archived = false))
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin("duck", "12345")
                .goToMenu()
                .goToProfilePage();

        ProfilePage profilePage = new ProfilePage();
        profilePage.checkThatCategoriesListContains(categoryJson.name());
    }
}
