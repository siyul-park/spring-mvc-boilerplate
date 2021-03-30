package io.github.siyual_park.model.user

data class UserCreatePayload(
    var name: String,
    var nickname: String,
    var password: String
)
