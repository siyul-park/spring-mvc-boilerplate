package io.github.siyual_park.exception

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class ExpiredTokenException(
    message: String = "Token is expired."
) : AuthenticationException(message)
