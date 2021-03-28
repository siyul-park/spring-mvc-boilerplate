package io.github.siyual_park.model.article

import io.github.siyual_park.model.UpdatePatch
import java.util.Optional

data class ArticleUpdatePayload(
    var title: Optional<String>? = null,
    var content: Optional<String>? = null,
) : UpdatePatch
