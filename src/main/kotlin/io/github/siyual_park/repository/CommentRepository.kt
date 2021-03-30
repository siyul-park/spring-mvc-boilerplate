package io.github.siyual_park.repository

import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.comment.Comment
import io.github.siyual_park.repository.base.CustomRepository
import io.github.siyual_park.repository.base.SimpleCustomRepository
import io.github.siyual_park.repository.specification.CommentSpecification
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Component
class CommentRepository(
    entityManager: EntityManager
) : CustomRepository<Comment, String, CommentSpecification> by SimpleCustomRepository.of(entityManager, CommentSpecification) {
    @Transactional
    fun findAllByArticles(articles: Iterable<Article>): List<Comment> {
        if (!articles.iterator().hasNext()) {
            return listOf()
        }
        return findAll({
            articles.map { withArticle(it) }
                .reduce { acc, specification -> acc.or(specification) }
        })
    }

    @Transactional
    fun findAllByArticleIds(articleIds: Iterable<String>): List<Comment> {
        if (!articleIds.iterator().hasNext()) {
            return listOf()
        }
        return findAll({
            articleIds.map { withArticle(it) }
                .reduce { acc, specification -> acc.or(specification) }
        })
    }

    @Transactional
    fun findAllByArticle(article: Article) = findAll({ withArticle(article) })

    @Transactional
    fun findAllByArticleId(articleId: String) = findAll({ withArticle(articleId) })

    @Transactional
    fun deleteAllByArticle(article: Article) = deleteAll { withArticle(article) }

    @Transactional
    fun deleteAllByArticleId(articleId: String) = deleteAll { withArticle(articleId) }
}
