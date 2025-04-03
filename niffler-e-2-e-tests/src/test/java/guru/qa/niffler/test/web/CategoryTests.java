package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class CategoryTests {

    private static final Config CFG = Config.getInstance();

    @Category(username = "duck", archived = true)
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin("duck", "12345")
                .goToMenu()
                .goToProfile()
                .showArchivedCategories();

        ProfilePage profilePage = new ProfilePage();
        profilePage.checkThatCategoriesListContains(categoryJson.name());
    }

    @Category(username = "duck", archived = false)
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .doLogin("duck", "12345")
                .goToMenu()
                .goToProfile();

        ProfilePage profilePage = new ProfilePage();
        profilePage.checkThatCategoriesListContains(categoryJson.name());
    }
}
