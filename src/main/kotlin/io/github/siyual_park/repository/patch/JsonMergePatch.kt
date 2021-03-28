package io.github.siyual_park.repository.patch

import com.fasterxml.jackson.databind.ObjectMapper

class JsonMergePatch<T>(
    private val overrides: Any,
    private val objectMapper: ObjectMapper
) : Patch<T> {

    override fun apply(obj: T): T = objectMapper.updateValue(obj, overrides)
}
