package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class UserDbClient {

    private final UserdataUserDao userDao = new UserdataUserDaoJdbc();

    public UserJson createUser(UserJson user) {
        UserEntity userEntity = UserEntity.fromJson(user);
        return UserJson.fromEntity(
                userDao.create(userEntity)
        );
    }

    public void deleteUser(UserJson user) {
        UserEntity userEntity = UserEntity.fromJson(user);
        userDao.delete(userEntity);
    }

    public UserJson findUserByUsername(String username) {
        return userDao.findByUsername(username)
                .map(UserJson::fromEntity).orElseThrow(
                        () -> new NoSuchElementException("Can`t find user.")
                );
    }

    public UserJson findUserById(UUID id) {
        return userDao.findById(id)
                .map(UserJson::fromEntity).orElseThrow(
                        () -> new NoSuchElementException("Can`t find user.")
                );
    }
}