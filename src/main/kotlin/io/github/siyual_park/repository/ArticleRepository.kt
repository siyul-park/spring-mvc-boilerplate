package io.github.siyual_park.repository

import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.repository.base.CustomRepository
import io.github.siyual_park.repository.base.SimpleCustomRepository
import io.github.siyual_park.repository.specification.ArticleSpecification
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Component
class ArticleRepository(
    entityManager: EntityManager
) : CustomRepository<Article, String, ArticleSpecification> by SimpleCustomRepository.of(entityManager, ArticleSpecification) {
    @Transactional
    fun findAllByCategory(category: Category) = findAll({ withCategory(category) })

    @Transactional
    fun findAllByCategory(categoryId: String) = findAll({ withCategory(categoryId) })

    @Transactional
    fun deleteAllByCategory(category: Category) = deleteAll { withCategory(category) }

    @Transactional
    fun deleteAllByCategory(categoryId: String) = deleteAll { withCategory(categoryId) }
}
