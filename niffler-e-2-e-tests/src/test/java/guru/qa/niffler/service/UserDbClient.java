package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;

import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.NoSuchElementException;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;


public class UserDbClient {

    private static final Config CFG = Config.getInstance();

    public UserJson createUser(UserJson user) {
        return transaction(connection -> {
            UserEntity userEntity = UserEntity.fromJson(user);
            return UserJson.fromEntity(new UserdataUserDaoJdbc(connection).create(userEntity));
        },
        CFG.userdataJdbcUrl(), 2);
    }

    public void deleteUser(UserJson user) {
         transaction(connection -> {
             UserEntity userEntity = UserEntity.fromJson(user);
             new UserdataUserDaoJdbc(connection).delete(userEntity);
         },
         CFG.userdataJdbcUrl(), 2);
    }

    public UserJson findUserByUsername(String username) {
        return transaction(connection -> {
            return new UserdataUserDaoJdbc(connection).findByUsername(username)
                    .map(UserJson::fromEntity).orElseThrow(
                            () -> new NoSuchElementException("Can`t find user.")
                    );
        }, CFG.userdataJdbcUrl(), 2);
    }

    public UserJson findUserById(UUID id) {
        return transaction(connection -> {
            return new UserdataUserDaoJdbc(connection).findById(id)
                    .map(UserJson::fromEntity).orElseThrow(
                            () -> new NoSuchElementException("Can`t find user.")
                    );
        }, CFG.userdataJdbcUrl(), 2);
    }
}