package io.github.siyual_park.converter.jackson.jsonView

import com.fasterxml.jackson.databind.module.SimpleModule
import io.github.siyual_park.model.jsonView.JsonView
import org.springframework.stereotype.Component

@Component
class JsonViewModule(
    jsonViewSerializer: JsonViewSerializer
) : SimpleModule() {
    init {
        this.addSerializer(JsonView::class.java, jsonViewSerializer)
    }
}
