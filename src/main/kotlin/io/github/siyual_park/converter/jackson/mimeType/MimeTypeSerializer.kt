package io.github.siyual_park.converter.jackson.mimeType

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.util.MimeType

class MimeTypeSerializer : JsonSerializer<MimeType>() {
    override fun serialize(value: MimeType, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeString(value.toString())
    }
}
