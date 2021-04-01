package io.github.siyual_park.exception

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UnsupportedAuthorizationTypeException(
    message: String = "Unsupported authorization type."
) : AuthenticationException(message)
