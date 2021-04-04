package io.github.siyual_park.model.category

import io.github.siyual_park.model.UpdatePayload
import java.util.Optional

data class CategoryUpdatePayload(
    var name: Optional<String>? = null,
) : UpdatePayload
