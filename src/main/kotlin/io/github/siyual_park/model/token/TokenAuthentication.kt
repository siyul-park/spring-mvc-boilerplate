package io.github.siyual_park.model.token

import io.github.siyual_park.model.user.User
import org.springframework.security.authentication.AbstractAuthenticationToken

data class TokenAuthentication(
    private val token: Token,
    private val user: User,
) : AbstractAuthenticationToken(token.scope) {
    init {
        isAuthenticated = true
    }

    override fun getCredentials() = token

    override fun getPrincipal() = user
}
