package io.github.siyual_park.model.token

import java.time.Instant
import java.util.UUID

data class Token(
    var userId: String,
    var expiredAt: Instant,
    var scope: Set<String> = setOf(),
    val id: String = UUID.randomUUID().toString(),
    val createdAt: Instant = Instant.now()
)
