package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.authUser.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
    AuthUserEntity create(AuthUserEntity user);
    List<AuthUserEntity> findAll();
    Optional<AuthUserEntity> findByUsername(String username);
    Optional<AuthUserEntity> findById(UUID id);
    AuthUserEntity update(AuthUserEntity user);
    void delete(AuthUserEntity user);
}