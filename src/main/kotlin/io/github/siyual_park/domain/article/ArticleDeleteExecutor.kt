package io.github.siyual_park.domain.article

import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.repository.ArticleRepository
import io.github.siyual_park.repository.CommentRepository
import org.springframework.stereotype.Component
import javax.persistence.LockModeType
import javax.transaction.Transactional

@Component
class ArticleDeleteExecutor(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
) {
    @Transactional
    fun execute(id: String) {
        val article = articleRepository.findByIdOrFail(id, LockModeType.PESSIMISTIC_WRITE)
        execute(article)
    }

    @Transactional
    fun execute(article: Article) {
        commentRepository.deleteAllByArticle(article)
        articleRepository.delete(article)
    }

    @Transactional
    fun execute(category: Category) {
        val articles = articleRepository.findAllByCategory(category, LockModeType.PESSIMISTIC_WRITE)
        val components = commentRepository.findAllByArticleIn(articles, LockModeType.PESSIMISTIC_WRITE)

        commentRepository.deleteAll(components)
        articleRepository.deleteAll(articles)
    }
}
