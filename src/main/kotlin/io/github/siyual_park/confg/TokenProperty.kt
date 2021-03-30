package io.github.siyual_park.confg

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app.token")
data class TokenProperty(
    val secret: String,
    val expiresIn: Long
)
