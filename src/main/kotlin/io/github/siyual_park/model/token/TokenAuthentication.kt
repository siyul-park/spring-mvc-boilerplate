package io.github.siyual_park.model.token

import io.github.siyual_park.model.user.User
import io.github.siyual_park.repository.UserRepository
import org.springframework.security.authentication.AbstractAuthenticationToken

class TokenAuthentication(
    private val token: Token,
    private val userRepository: UserRepository,
) : AbstractAuthenticationToken(token.scope) {
    private var user: User? = null

    override fun getCredentials(): Any {
        return token
    }

    override fun getPrincipal(): Any {
        return if (user != null) {
            user
        } else {
            user = userRepository.findByIdOrFail(token.userId)
            user
        } as User
    }
}
