package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {

  private final ElementsCollection categoriesList = $$("div[role='button']");
  private final SelenideElement archivedToggle = $("input[type='checkbox']");

  public void showArchivedCategories() {
    archivedToggle.click();
  }

  public void checkThatCategoriesListContains(String category) {
    categoriesList.find(text(category))
            .should(visible);
  }
}
