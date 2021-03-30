package io.github.siyual_park.model.article

import org.springframework.util.MimeType
import java.time.Instant

data class ArticleResponsePayload(
    var id: String,
    var categoryId: String,

    var title: String,

    var content: String,
    var contentType: MimeType,

    var views: Long,

    var createdAt: Instant,
    var updatedAt: Instant?
) {
    companion object {
        fun from(article: Article) = with(article) {
            ArticleResponsePayload(
                id!!,
                category.id!!,
                title,
                content,
                contentType,
                views,
                createdAt!!,
                updatedAt
            )
        }
    }
}
