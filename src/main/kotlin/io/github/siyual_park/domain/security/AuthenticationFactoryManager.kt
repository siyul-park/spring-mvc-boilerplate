package io.github.siyual_park.domain.security

import io.github.siyual_park.exception.UnsupportedAuthorizationTypeException
import org.springframework.stereotype.Component

@Component
class AuthenticationFactoryManager(
    private val authenticationFactories: MutableSet<AuthenticationFactory> = mutableSetOf()
) {
    fun get(type: String): AuthenticationFactory {
        return authenticationFactories.find { it.supports(type) }
            ?: throw UnsupportedAuthorizationTypeException()
    }

    fun register(authenticationFactory: AuthenticationFactory) {
        authenticationFactories.add(authenticationFactory)
    }
}
