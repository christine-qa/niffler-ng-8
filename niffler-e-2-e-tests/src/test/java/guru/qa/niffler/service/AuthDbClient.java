package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.authUser.AuthUserEntity;
import guru.qa.niffler.data.entity.authUser.Authority;
import guru.qa.niffler.data.entity.authUser.AuthorityEntity;

import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.*;

import java.util.Optional;

import static guru.qa.niffler.data.Databases.xaTransaction;

public class AuthDbClient {

    private static final Config CFG = Config.getInstance();

    public UserJson createUser(UserJson user) {
       return xaTransaction(2,
                new Databases.XaFunction<> (connection -> {
                    AuthUserDao authUserDao = new AuthUserDaoJdbc(connection);
                    AuthUserEntity authUserEntity = new AuthUserEntity();
                    Optional<AuthUserEntity> userEntity = authUserDao.findByUsername(user.username());

                    if (userEntity.isPresent()) {
                        throw new IllegalStateException("User with such username already exists!");
                    } else {
                        authUserEntity.setUsername(user.username());
                        authUserEntity.setPassword("12345");
                        authUserEntity.setEnabled(true);
                        authUserEntity.setAccountNonExpired(true);
                        authUserEntity.setAccountNonLocked(true);
                        authUserEntity.setCredentialsNonExpired(true);
                        authUserEntity = new AuthUserDaoJdbc(connection).create(
                                 authUserEntity);
                    }
                    AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc(connection);
                    for (Authority authority : Authority.values()) {
                        AuthorityEntity authorityEntity = new AuthorityEntity();
                        authorityEntity.setUser(authUserEntity);
                        authorityEntity.setAuthority(authority);
                        authAuthorityDao.create(authorityEntity);
                    }
                    return null;
                },
                CFG.authJdbcUrl()
                ),
                new Databases.XaFunction<>(connection -> {
                        UserEntity userEntity = UserEntity.fromJson(user);
                        return UserJson.fromEntity(new UserdataUserDaoJdbc(connection).create(userEntity));
                }, CFG.userdataJdbcUrl()
            )
        );
    }
}
