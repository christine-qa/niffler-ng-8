package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.authUser.AuthorityEntity;

import java.util.List;
import java.util.UUID;

public interface AuthAuthorityDao {
    AuthorityEntity create(AuthorityEntity authority);
    List<AuthorityEntity> findAuthorityByUserId(UUID id);
    AuthorityEntity updateAuthority(AuthorityEntity authority);
    void delete(AuthorityEntity authority);
}