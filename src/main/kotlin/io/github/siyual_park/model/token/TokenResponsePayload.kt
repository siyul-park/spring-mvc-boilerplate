package io.github.siyual_park.model.token

data class TokenResponsePayload(
    var accessToken: String,
    var tokenType: String,
    var expiresIn: Long
)
