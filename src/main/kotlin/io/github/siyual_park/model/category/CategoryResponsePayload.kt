package io.github.siyual_park.model.category

import io.github.siyual_park.model.user.UserResponsePayload
import java.time.Instant

data class CategoryResponsePayload(
    val id: String,

    var name: String,

    var owner: UserResponsePayload,

    val createdAt: Instant,
    val updatedAt: Instant?
)
