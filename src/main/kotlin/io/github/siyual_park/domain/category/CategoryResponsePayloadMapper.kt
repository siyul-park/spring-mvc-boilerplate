package io.github.siyual_park.domain.category

import io.github.siyual_park.model.Mapper
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.model.category.CategoryResponsePayload
import org.springframework.stereotype.Component

@Component
class CategoryResponsePayloadMapper : Mapper<Category, CategoryResponsePayload> {
    override fun map(input: Category) = CategoryResponsePayload.from(input)
}
