package io.github.siyual_park.domain.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.siyual_park.exception.InvalidAuthorizationHeaderFormatException
import io.github.siyual_park.model.ErrorResponse
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import java.lang.RuntimeException
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationFilter(
    private val authenticationFactoryManager: AuthenticationFactoryManager,
    private val objectMapper: ObjectMapper
) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        try {
            httpRequest.getHeader("Authorization")?.let {
                val tokens = it.split(" ")
                if (tokens.size != 2) {
                    throw InvalidAuthorizationHeaderFormatException()
                }

                val type = tokens[0]
                val credentials = tokens[1]

                val authenticationFactory = authenticationFactoryManager.get(type)
                SecurityContextHolder.getContext().authentication = authenticationFactory.create(credentials)
            }
        } catch (e: RuntimeException) {
            httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED
            httpResponse.contentType = MediaType.APPLICATION_JSON.toString()
            httpResponse.writer.write(objectMapper.writeValueAsString(ErrorResponse.from(e)))
            return
        }

        chain.doFilter(request, response)
    }
}
