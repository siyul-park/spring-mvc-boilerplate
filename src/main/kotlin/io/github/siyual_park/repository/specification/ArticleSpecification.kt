package io.github.siyual_park.repository.specification

import io.github.siyual_park.exception.ConflictException
import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.repository.expansion.get
import org.springframework.data.jpa.domain.Specification

object ArticleSpecification {
    fun withCategory(category: Category) = category.id?.let { withCategory(it) } ?: throw ConflictException()

    fun withCategory(categoryId: String) = Specification<Article> { root, _, builder ->
        builder.equal(root[Article::category][Category::id], categoryId)
    }
}
