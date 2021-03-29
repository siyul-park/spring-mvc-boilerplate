package io.github.siyual_park.repository.specification

import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.repository.expansion.get
import org.springframework.data.jpa.domain.Specification

object ArticleSpecification {
    fun withCategory(categoryId: String) = Specification<Article> { root, _, builder ->
        builder.equal(root[Article::category][Category::id], categoryId)
    }
}
