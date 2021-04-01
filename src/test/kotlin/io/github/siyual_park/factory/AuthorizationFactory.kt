package io.github.siyual_park.factory

import io.github.siyual_park.domain.security.TokenExchanger
import io.github.siyual_park.domain.security.TokenFactory
import io.github.siyual_park.model.user.User
import org.springframework.stereotype.Component

@Component
class AuthorizationFactory(
    private val tokenExchanger: TokenExchanger,
    private val tokenFactory: TokenFactory,
) {
    fun create(user: User): String {
        val token = tokenFactory.createAccessToken(user)
        return "bearer ${tokenExchanger.encode(token)}"
    }
}
