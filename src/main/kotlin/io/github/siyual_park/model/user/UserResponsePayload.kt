package io.github.siyual_park.model.user

import com.fasterxml.jackson.annotation.JsonView
import java.time.Instant

data class UserResponsePayload(
    @JsonView(Public::class)
    var id: String,

    @JsonView(Public::class)
    var name: String,

    @JsonView(Public::class)
    var nickname: String,

    @JsonView(Private::class)
    val scope: String,

    @JsonView(Public::class)
    val createdAt: Instant,
    @JsonView(Public::class)
    val updatedAt: Instant?
) {
    interface Public
    interface Private : Public
}
