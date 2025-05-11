package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;

import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.authUser.AuthUserEntity;
import guru.qa.niffler.data.entity.authUser.Authority;
import guru.qa.niffler.data.entity.authUser.AuthorityEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.*;

public class UserDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

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

    public UserJson createUser(UserJson user) {
        return UserJson.fromEntity(
                xaTransaction(4,
                        new Databases.XaFunction<>(
                                con -> {
                                    AuthUserEntity authUser = new AuthUserEntity();
                                    authUser.setUsername(user.username());
                                    authUser.setPassword(pe.encode("12345"));
                                    authUser.setEnabled(true);
                                    authUser.setAccountNonExpired(true);
                                    authUser.setAccountNonLocked(true);
                                    authUser.setCredentialsNonExpired(true);
                                    new AuthUserDaoJdbc(con).create(authUser);
                                    new AuthAuthorityDaoJdbc(con).create(
                                            Arrays.stream(Authority.values())
                                                    .map(a -> {
                                                                AuthorityEntity ae = new AuthorityEntity();
                                                                ae.setUserId(authUser.getId());
                                                                ae.setAuthority(a);
                                                                return ae;
                                                            }
                                                    ).toArray(AuthorityEntity[]::new));
                                    return null;
                                },
                                CFG.authJdbcUrl()
                        ),
                        new Databases.XaFunction<>(
                                con -> {
                                    UserEntity ue = new UserEntity();
                                    ue.setUsername(user.username());
                                    ue.setFullname(user.fullname());
                                    ue.setCurrency(user.currency());
                                    new UserdataUserDaoJdbc(con).create(ue);
                                    return ue;
                                },
                                CFG.userdataJdbcUrl()
                        )
                )
        );
    }

    public UserJson createUserSpringJdbc(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authorityEntities);

        return UserJson.fromEntity(
                new UserdataUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
                        .create(
                                UserEntity.fromJson(user)
                        )
        );
    }
}