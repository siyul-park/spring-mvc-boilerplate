package io.github.siyual_park.repository.specification

import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.category.Category
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.Path

object ArticleSpecification {
    fun withCategory(categoryId: String) = Specification<Article> { root, _, builder ->
        builder.equal(
            root.get<Path<Category>>(Article::category.name).get<String>(Category::id.name),
            categoryId
        )
    }
}
