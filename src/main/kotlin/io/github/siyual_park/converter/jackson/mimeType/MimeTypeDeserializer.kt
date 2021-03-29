package io.github.siyual_park.converter.jackson.mimeType

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.util.MimeType

class MimeTypeDeserializer : JsonDeserializer<MimeType>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): MimeType {
        return MimeType.valueOf(p.valueAsString)
    }
}
