package io.github.siyual_park.expansion

import org.springframework.security.core.GrantedAuthority

fun Collection<GrantedAuthority>.has(authority: String): Boolean {
    return this.find { it.authority == authority } != null
}
