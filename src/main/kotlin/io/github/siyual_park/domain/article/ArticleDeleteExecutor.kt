package io.github.siyual_park.domain.article

import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.repository.ArticleRepository
import io.github.siyual_park.repository.CommentRepository
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class ArticleDeleteExecutor(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
) {
    @Transactional
    fun execute(id: String) {
        val article = articleRepository.findByIdOrFail(id)
        execute(article)
    }

    @Transactional
    fun execute(article: Article) {
        commentRepository.deleteAllByArticle(article)
        articleRepository.delete(article)
    }

    @Transactional
    fun execute(category: Category) {
        val articles = articleRepository.findAllByCategory(category)
        val components = commentRepository.findAllByArticles(articles)

        commentRepository.deleteAll(components)
        articleRepository.deleteAll(articles)
    }
}
