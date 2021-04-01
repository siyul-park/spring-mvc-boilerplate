package io.github.siyual_park.domain.security

import io.github.siyual_park.domain.scope.ScopeFetchExecutor
import io.github.siyual_park.model.scope.Scope
import io.github.siyual_park.model.token.Token
import io.github.siyual_park.model.user.User
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class TokenFactory(
    private val scopeFetchExecutor: ScopeFetchExecutor
) {
    fun create(user: User, expiresIn: Long, scope: Scope? = null): Token {
        val finalScope = if (scope != null) {
            val allScope = scopeFetchExecutor.execute(user)
            scope.filter { allScope.contains(it) }.toSet()
        } else {
            scopeFetchExecutor.execute(user, 0)
        }

        return Token(
            user.id!!,
            Instant.now().plus(Duration.ofSeconds(expiresIn)),
            finalScope
        )
    }
}
