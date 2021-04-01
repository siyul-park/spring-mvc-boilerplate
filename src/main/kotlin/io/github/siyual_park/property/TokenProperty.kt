package io.github.siyual_park.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "application.token")
data class TokenProperty(
    val secret: String,
    val expiresIn: Long
)
