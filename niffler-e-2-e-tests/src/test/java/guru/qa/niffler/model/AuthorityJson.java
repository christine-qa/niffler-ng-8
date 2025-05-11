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
) {}
