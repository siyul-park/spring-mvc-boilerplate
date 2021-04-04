package io.github.siyual_park.domain.category

import io.github.siyual_park.model.Mapper
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.model.category.CategoryCreatePayload
import org.springframework.stereotype.Component

@Component
class CategoryCreatePayloadMapper : Mapper<CategoryCreatePayload, Category> {
    override fun map(input: CategoryCreatePayload) = input.toCategory()
}
