package io.github.siyual_park.converter.jackson.mimeType

import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.util.MimeType

class MimeTypeModule : SimpleModule() {
    init {
        addSerializer(MimeType::class.java, MimeTypeSerializer())
        addDeserializer(MimeType::class.java, MimeTypeDeserializer())
    }
}
