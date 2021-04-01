package io.github.siyual_park.domain.category

import io.github.siyual_park.domain.Mapper
import io.github.siyual_park.domain.user.NotFetchScopeUserResponsePayloadMapper
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.model.category.CategoryResponsePayload
import org.springframework.stereotype.Component

@Component
class CategoryResponsePayloadMapper(
    private val notFetchScopeUserResponsePayloadMapper: NotFetchScopeUserResponsePayloadMapper
) : Mapper<Category, CategoryResponsePayload> {
    override fun map(input: Category) = with(input) {
        CategoryResponsePayload(
            id!!,
            name,
            notFetchScopeUserResponsePayloadMapper.map(owner),
            createdAt!!,
            updatedAt
        )
    }
}
