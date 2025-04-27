package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);

        CategoryEntity category = categoryDao.findCategoryByUsernameAndCategoryName(spendEntity.getUsername(),
                spendEntity.getCategory().getName()).orElseGet(
                () -> categoryDao.create(spendEntity.getCategory())
                );

        spendEntity.setCategory(category);
        return SpendJson.fromEntity(spendDao.create(spendEntity));
    }



    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        return CategoryJson.fromEntity(
                categoryDao.create(categoryEntity)
        );
    }

    public void updateCategory(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        categoryDao.updateCategory(categoryEntity);
    }

    public void delete(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        spendDao.deleteSpend(spendEntity);
    }

    public List<SpendJson> findAllByUsername(String username) {
        List<SpendEntity> spendEntityList = spendDao.findAllByUsername(username);
        return spendEntityList.stream().map(SpendJson::fromEntity).toList();
    }

    public SpendJson findSpendById(UUID id) {
        return spendDao.findSpendById(id).map(SpendJson::fromEntity).orElseThrow(
                () -> new NoSuchElementException("Can`t find spend.")
        );
    }

    public CategoryJson findCategoryByUsernameAndCategoryName(String username, String category) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, category)
                .map(CategoryJson::fromEntity).orElseThrow(
                        () -> new NoSuchElementException("Can`t find category.")
                );
    }

    public List<CategoryJson> findAllCategoriesByUsername(String username) {
        List<CategoryEntity> ce =  categoryDao.findAllByUsername(username);
        return ce.stream().map(CategoryJson::fromEntity).toList();
    }

    public void deleteCategory(CategoryJson json) {
        CategoryEntity ce = CategoryEntity.fromJson(json);
        categoryDao.deleteCategory(ce);
    }
}