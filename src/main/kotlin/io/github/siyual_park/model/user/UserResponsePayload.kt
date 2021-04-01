package io.github.siyual_park.model.user

import com.fasterxml.jackson.annotation.JsonView
import io.github.siyual_park.model.View
import java.time.Instant

data class UserResponsePayload(
    @JsonView(View.Public::class)
    var id: String,

    @JsonView(View.Public::class)
    var name: String,

    @JsonView(View.Public::class)
    var nickname: String,

    @JsonView(View.Private::class)
    val scope: String?,

    @JsonView(View.Public::class)
    val createdAt: Instant,
    @JsonView(View.Public::class)
    val updatedAt: Instant?
)
