package io.github.siyual_park.domain.category

import io.github.siyual_park.domain.article.ArticleDeleteExecutor
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.repository.CategoryRepository
import org.springframework.stereotype.Component
import javax.persistence.LockModeType
import javax.transaction.Transactional

@Component
class CategoryDeleteExecutor(
    private val categoryRepository: CategoryRepository,
    private val articleDeleteExecutor: ArticleDeleteExecutor,
) {
    @Transactional
    fun execute(id: String) {
        val category = categoryRepository.findByIdOrFail(id, LockModeType.PESSIMISTIC_WRITE)
        execute(category)
    }

    @Transactional
    fun execute(category: Category) {
        articleDeleteExecutor.execute(category)
        categoryRepository.delete(category)
    }
}
