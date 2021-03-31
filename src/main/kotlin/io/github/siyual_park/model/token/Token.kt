package io.github.siyual_park.model.token

import io.github.siyual_park.model.scope.ScopeToken
import java.time.Instant
import java.util.UUID

data class Token(
    var userId: String,
    var expiredAt: Instant,
    var scope: Set<ScopeToken> = setOf(),
    val id: String = UUID.randomUUID().toString(),
    val createdAt: Instant = Instant.now()
) {
    fun isValid(): Boolean {
        return expiredAt.isBefore(Instant.now())
    }
}
