package guru.qa.niffler.data.entity.authUser;

import guru.qa.niffler.model.AuthUserJson;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthUserEntity implements Serializable {
  private UUID id;
  private String username;
  private String password;
  private Boolean enabled;
  private Boolean accountNonExpired;
  private Boolean accountNonLocked;
  private Boolean credentialsNonExpired;

  public static AuthUserEntity fromJson(AuthUserJson json) {
    AuthUserEntity userEntity = new AuthUserEntity();
    userEntity.setId(json.id());
    userEntity.setUsername(json.username());
    userEntity.setPassword(json.password());
    userEntity.setEnabled(json.enabled());
    userEntity.setAccountNonExpired(json.accountNonExpired());
    userEntity.setAccountNonLocked(json.accountNonLocked());
    userEntity.setCredentialsNonExpired(json.credentialsNonExpired());
    return userEntity;
  }
}
