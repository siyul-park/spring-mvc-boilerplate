package io.github.siyual_park.model.category

import io.github.siyual_park.model.Mapper

class CategoryResponsePayloadMapper : Mapper<Category, CategoryResponsePayload> {
    override fun map(input: Category) = CategoryResponsePayload.from(input)
}
