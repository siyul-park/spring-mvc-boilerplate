package io.github.siyual_park.repository.specification

import io.github.siyual_park.exception.ConflictException
import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.comment.Comment
import io.github.siyual_park.repository.expansion.get
import org.springframework.data.jpa.domain.Specification

object CommentSpecification {
    fun withArticle(article: Article) = article.id?.let { withArticle(it) } ?: throw ConflictException()

    fun withArticle(articleId: String) = Specification<Comment> { root, _, builder ->
        builder.equal(root[Comment::article][Article::id], articleId)
    }
}
