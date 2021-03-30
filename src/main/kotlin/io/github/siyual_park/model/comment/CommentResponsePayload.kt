package io.github.siyual_park.model.comment

import org.springframework.util.MimeType
import java.time.Instant

data class CommentResponsePayload(
    val id: String,

    var articleId: String,

    var content: String,
    var contentType: MimeType,

    val createdAt: Instant,
    val updatedAt: Instant?
) {
    companion object {
        fun from(comment: Comment) = with(comment) {
            CommentResponsePayload(
                id!!,
                article.id!!,
                content,
                contentType,
                createdAt!!,
                updatedAt
            )
        }
    }
}
