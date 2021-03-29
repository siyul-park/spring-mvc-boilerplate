package io.github.siyual_park.factory

import io.github.siyual_park.model.category.CategoryCreatePayload

class CategoryCreatePayloadMockFactory : MockFactory<CategoryCreatePayload> {
    private var count: Int = 0

    override fun create(): CategoryCreatePayload {
        val count = count++
        return CategoryCreatePayload(
            "${RandomFactory.createString(10)}-$count"
        )
    }
}
