package guru.qa.niffler.data.entity.authUser;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.AuthorityJson;
import guru.qa.niffler.model.SpendJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthorityEntity implements Serializable {
  private UUID id;
  private Authority authority;
  private AuthUserEntity user;

  public static AuthorityEntity fromJson(AuthorityJson json) {
    AuthorityEntity authorityEntity = new AuthorityEntity();
    authorityEntity.setId(json.id());
    authorityEntity.setUser(AuthUserEntity.fromJson(json.authUserJson()));
    authorityEntity.setAuthority(json.authority());
    return authorityEntity;
  }
}
