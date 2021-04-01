package io.github.siyual_park.model.user

import com.fasterxml.jackson.annotation.JsonView
import java.time.Instant

data class UserResponsePayload(
    @JsonView(IdScope::class)
    var id: String,

    @JsonView(NameScope::class)
    var name: String,

    @JsonView(NickNameScope::class)
    var nickname: String,

    @JsonView(ScopeScope::class)
    val scope: String,

    @JsonView(CreatedAtScope::class)
    val createdAt: Instant,
    @JsonView(UpdatedAtScope::class)
    val updatedAt: Instant?
) {
    interface Public : IdScope, NameScope, NickNameScope, CreatedAtScope, UpdatedAtScope
    interface Private : Public, ScopeScope
}
