package io.github.siyual_park.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import io.github.siyual_park.converter.jackson.instant.InstantEpochTimeModule
import io.github.siyual_park.converter.jackson.mimeType.MimeTypeModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfiguration {

    @Autowired(required = true)
    fun configObjectMapper(objectMapper: ObjectMapper) {
        objectMapper.apply {
            registerModule(Jdk8Module())
            registerModule(InstantEpochTimeModule())
            registerModule(MimeTypeModule())

            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        }
    }
}
