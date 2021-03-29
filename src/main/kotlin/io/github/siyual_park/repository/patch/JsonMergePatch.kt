package io.github.siyual_park.repository.patch

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.siyual_park.exception.BadRequestException

class JsonMergePatch<T>(
    private val overrides: Any,
    private val objectMapper: ObjectMapper
) : Patch<T> {

    override fun apply(obj: T): T = try {
        objectMapper.updateValue(obj, overrides)
    } catch (e: JsonMappingException) {
        throw BadRequestException(e.message)
    }
}
