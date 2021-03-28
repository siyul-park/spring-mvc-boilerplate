package io.github.siyual_park.repository.patch

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class JsonMergePatchFactory(
    private val objectMapper: ObjectMapper
) {
    fun <O : Any, T : Any> create(overrides: O) = JsonMergePatch<T>(overrides, objectMapper)
}
