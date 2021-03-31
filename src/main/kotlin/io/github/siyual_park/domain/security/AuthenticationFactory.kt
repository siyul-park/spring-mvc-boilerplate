package io.github.siyual_park.domain.security

import org.springframework.security.core.Authentication

interface AuthenticationFactory {
    fun create(credentials: String): Authentication

    fun supports(type: String): Boolean
}
