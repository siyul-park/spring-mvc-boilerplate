package io.github.siyual_park.domain.comment

import io.github.siyual_park.model.Mapper
import io.github.siyual_park.model.comment.Comment
import io.github.siyual_park.model.comment.CommentCreatePayload
import io.github.siyual_park.repository.ArticleRepository
import org.springframework.stereotype.Component

@Component
class CommentCreatePayloadMapper(
    private val articleRepository: ArticleRepository
) : Mapper<CommentCreatePayload, Comment> {
    override fun map(input: CommentCreatePayload): Comment {
        val article = articleRepository.findByIdOrFail(input.articleId)
        return Comment(input.content, input.contentType, article)
    }
}
