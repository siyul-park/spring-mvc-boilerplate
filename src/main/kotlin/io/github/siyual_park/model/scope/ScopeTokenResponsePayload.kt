package io.github.siyual_park.model.scope

import java.time.Instant

data class ScopeTokenResponsePayload(
    val id: String,

    var name: String,
    var description: String?,

    var children: Set<ScopeTokenResponsePayload>?,

    val createdAt: Instant,
    val updatedAt: Instant?
)
