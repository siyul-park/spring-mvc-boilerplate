package io.github.siyual_park.model.user

import java.time.Instant

data class UserResponsePayload(
    var id: String,

    var name: String,
    var nickname: String,

    val createdAt: Instant,
    val updatedAt: Instant?
) {
    companion object {
        fun from(user: User) = with(user) {
            UserResponsePayload(
                id!!,
                name,
                nickname,
                createdAt!!,
                updatedAt
            )
        }
    }
}
