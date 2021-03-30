package io.github.siyual_park.factory

import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.comment.CommentCreatePayload
import org.springframework.util.MimeType

class CommentCreatePayloadMockFactory(
    private val article: Article
) : MockFactory<CommentCreatePayload> {
    private var count: Int = 0

    override fun create(): CommentCreatePayload {
        val count = count++
        return CommentCreatePayload(
            "${RandomFactory.createString(10)}-$count",
            MimeType.valueOf("text/plain"),
            article.id!!
        )
    }
}
