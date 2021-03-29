package io.github.siyual_park.model

import java.time.Instant

data class ErrorResponse(
    val message: String,
    val timestamp: Instant = Instant.now()
)
