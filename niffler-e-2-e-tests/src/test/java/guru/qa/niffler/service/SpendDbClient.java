package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    CategoryDao categoryDao =  new CategoryDaoJdbc(connection);
                    CategoryEntity category = categoryDao.findCategoryByUsernameAndCategoryName(spendEntity.getUsername(),
                            spendEntity.getCategory().getName()).orElseGet(
                            () -> categoryDao.create(spendEntity.getCategory())
                    );
                    spendEntity.setCategory(category);
                    return SpendJson.fromEntity(new SpendDaoJdbc(connection).create(spendEntity));
                },
                CFG.spendJdbcUrl(),2
            );
        }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(
                    new CategoryDaoJdbc(connection).create(categoryEntity)
                    );
                },
                CFG.spendJdbcUrl(), 2
        );
    }

    public void updateCategory(CategoryJson category) {
        transaction(connection -> {
                CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                new CategoryDaoJdbc(connection).updateCategory(categoryEntity);

        },
                CFG.spendJdbcUrl(),
                2
        );
    }

    public void delete(SpendJson spend) {
        transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            new SpendDaoJdbc(connection).deleteSpend(spendEntity);
        },
        CFG.spendJdbcUrl(),
        2);
    }

    public List<SpendJson> findAllByUsername(String username) {
        return transaction(connection -> {
            List<SpendEntity> spendEntityList = new SpendDaoJdbc(connection).findAllByUsername(username);
            return spendEntityList.stream().map(SpendJson::fromEntity).toList();
        },
        CFG.spendJdbcUrl(),
        2);
    }

    public SpendJson findSpendById(UUID id) {
        return transaction(connection -> {
            return new SpendDaoJdbc(connection).findSpendById(id).map(SpendJson::fromEntity).orElseThrow(
                () -> new NoSuchElementException("Can`t find spend.")
            );
        },
        CFG.spendJdbcUrl(),
        2);

    }

    public CategoryJson findCategoryByUsernameAndCategoryName(String username, String category) {
        return transaction(connection -> {
            return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, category)
                    .map(CategoryJson::fromEntity).orElseThrow(
                    );
            },
            CFG.spendJdbcUrl(),
            2);
    }

    public List<CategoryJson> findAllCategoriesByUsername(String username) {
        return transaction(connection -> {
            List<CategoryEntity> ce =  new CategoryDaoJdbc(connection).findAllByUsername(username);
            return ce.stream().map(CategoryJson::fromEntity).toList();
        },
        CFG.spendJdbcUrl(),
        2
        );
    }

    public void deleteCategory(CategoryJson json) {
        transaction(connection -> {
            CategoryEntity ce = CategoryEntity.fromJson(json);
            new CategoryDaoJdbc(connection).deleteCategory(ce);
        },
        CFG.spendJdbcUrl(),
        2);
    }
}