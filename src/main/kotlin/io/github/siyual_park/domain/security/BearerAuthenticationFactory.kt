package io.github.siyual_park.domain.security

import io.github.siyual_park.exception.ExpiredTokenException
import io.github.siyual_park.model.token.TokenAuthentication
import io.github.siyual_park.repository.CachedUserRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class BearerAuthenticationFactory(
    private val tokenExchanger: TokenExchanger,
    private val userRepository: CachedUserRepository
) : AuthenticationFactory {
    override fun create(credentials: String): Authentication {
        val token = tokenExchanger.decode(credentials)
        if (!token.isValid()) {
            throw ExpiredTokenException()
        }
        val user = userRepository.findByIdOrFail(token.userId)

        return TokenAuthentication(token, user)
    }

    override fun supports(type: String): Boolean {
        return type.toLowerCase() == "bearer"
    }
}
