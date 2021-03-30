package io.github.siyual_park.repository.specification

import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.comment.Comment
import io.github.siyual_park.repository.expansion.get
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.jpa.domain.Specification

object CommentSpecification {
    fun withArticles(articles: Iterable<Article>): Specification<Comment> {
        return withArticleIds(articles.map { it.id }.filterNotNull())
    }

    fun withArticleIds(articleIds: Iterable<String>) = Specification<Comment> { root, _, builder ->
        builder.`in`(root[Comment::article][Article::id]).apply {
            articleIds.forEach { value(it) }
        }
    }

    fun withArticle(article: Article) = article.id?.let { withArticle(it) } ?: throw EmptyResultDataAccessException("No ${Article::class.java} entity exists!", 1)

    fun withArticle(articleId: String) = Specification<Comment> { root, _, builder ->
        builder.equal(root[Comment::article][Article::id], articleId)
    }
}
