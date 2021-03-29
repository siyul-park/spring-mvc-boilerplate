package io.github.siyual_park.repository.specification

import io.github.siyual_park.model.article.Article
import org.springframework.data.jpa.domain.Specification

object ArticleSpecification {
    fun withCategory(categoryId: String) = Specification<Article> { root, _, builder ->
        builder.equal(root.get<String>("category_id"), categoryId)
    }
}
