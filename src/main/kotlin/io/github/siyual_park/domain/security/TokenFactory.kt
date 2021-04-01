package io.github.siyual_park.domain.security

import io.github.siyual_park.confg.PreDefinedScope
import io.github.siyual_park.domain.scope.ScopeFetchExecutor
import io.github.siyual_park.exception.AccessDeniedException
import io.github.siyual_park.model.scope.Scope
import io.github.siyual_park.model.scope.has
import io.github.siyual_park.model.token.Token
import io.github.siyual_park.model.user.User
import io.github.siyual_park.property.TokenProperty
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class TokenFactory(
    private val scopeFetchExecutor: ScopeFetchExecutor,
    private val tokenProperty: TokenProperty
) {
    fun createAccessToken(user: User): Token {
        val scope = scopeFetchExecutor.execute(user).also {
            if (!it.has(PreDefinedScope.accessToken.create)) {
                throw AccessDeniedException()
            }
        }
            .filter { it.name != PreDefinedScope.accessToken.create }
            .toSet()

        return create(user, tokenProperty.expiresIn, scope = scope)
    }

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
