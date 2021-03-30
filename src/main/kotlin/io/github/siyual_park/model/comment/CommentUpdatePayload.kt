package io.github.siyual_park.model.comment

import io.github.siyual_park.model.UpdatePayload
import org.springframework.util.MimeType
import java.util.Optional

data class CommentUpdatePayload(
    var content: Optional<String>? = null,
    var contentType: Optional<MimeType>? = null,
) : UpdatePayload
