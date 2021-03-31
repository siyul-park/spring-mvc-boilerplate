package io.github.siyual_park.filter

import io.github.siyual_park.domain.security.TokenExchanger
import io.github.siyual_park.exception.ExpiredTokenException
import io.github.siyual_park.exception.InvalidAuthorizationHeaderFormatException
import io.github.siyual_park.exception.UnsupportedAuthorizationTypeException
import io.github.siyual_park.model.token.TokenAuthentication
import io.github.siyual_park.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class AuthenticationFilter(
    private val tokenExchanger: TokenExchanger,
    private val userRepository: UserRepository
) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest

        httpRequest.getHeader("Authorization")?.let {
            val tokens = it.split(" ")
            if (tokens.size != 2) {
                throw InvalidAuthorizationHeaderFormatException()
            }

            val type = tokens[0]
            val credentials = tokens[1]

            when (type.toLowerCase()) {
                "bearer" -> {
                    val token = tokenExchanger.decode(credentials)
                    if (!token.isValid()) {
                        throw ExpiredTokenException()
                    }

                    val authentication = TokenAuthentication(token, userRepository)
                    SecurityContextHolder.getContext().authentication = authentication
                }
                else -> throw UnsupportedAuthorizationTypeException()
            }
        }

        chain.doFilter(request, response)
    }
}
