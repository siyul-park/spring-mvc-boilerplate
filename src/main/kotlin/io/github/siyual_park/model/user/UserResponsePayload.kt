package io.github.siyual_park.model.user

import java.time.Instant

data class UserResponsePayload(
    var id: String,

    var name: String,
    var nickname: String,

    val scope: String,

    val createdAt: Instant,
    val updatedAt: Instant?
)
