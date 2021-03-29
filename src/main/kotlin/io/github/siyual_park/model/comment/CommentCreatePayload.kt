package io.github.siyual_park.model.comment

import org.springframework.util.MimeType

data class CommentCreatePayload(
    var content: String,
    var contentType: MimeType,
    var articleId: String
)
