package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.authUser.AuthUserEntity;
import guru.qa.niffler.data.entity.authUser.Authority;
import guru.qa.niffler.data.entity.authUser.AuthorityEntity;

import java.util.UUID;

public record AuthorityJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("user")
    AuthUserJson authUserJson,
    @JsonProperty("authority")
    Authority authority
) {
    public static AuthorityJson fromEntity(AuthorityEntity entity) {
        AuthUserEntity userEntity = entity.getUser();
        return new AuthorityJson(
                entity.getId(),
                new AuthUserJson(
                        userEntity.getId(),
                        userEntity.getUsername(),
                        userEntity.getPassword(),
                        userEntity.getEnabled(),
                        userEntity.getAccountNonExpired(),
                        userEntity.getAccountNonLocked(),
                        userEntity.getCredentialsNonExpired()),
                entity.getAuthority()
        );
    }
}
