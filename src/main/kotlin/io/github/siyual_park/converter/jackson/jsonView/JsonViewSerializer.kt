package io.github.siyual_park.converter.jackson.jsonView

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import io.github.siyual_park.model.jsonView.JsonView
import org.springframework.stereotype.Component

@Component
class JsonViewSerializer(
    private val objectMapper: ObjectMapper
) : JsonSerializer<JsonView<*>>() {
    override fun serialize(value: JsonView<*>, gen: JsonGenerator, serializers: SerializerProvider) {
        val jsonNode = objectMapper.readTree(
            if (value.view == null) {
                objectMapper.writeValueAsString(value.value)
            } else {
                objectMapper.writerWithView(value.view.java).writeValueAsString(value.value)
            }
        )
        gen.writeObject(jsonNode)
    }
}
