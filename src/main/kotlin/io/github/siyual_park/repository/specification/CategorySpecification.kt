package io.github.siyual_park.repository.specification

import io.github.siyual_park.model.category.Category
import io.github.siyual_park.repository.expansion.get
import org.springframework.data.jpa.domain.Specification

object CategorySpecification {
    fun withName(name: String) = Specification<Category> { root, _, builder ->
        builder.equal(root[Category::name], name)
    }
}
