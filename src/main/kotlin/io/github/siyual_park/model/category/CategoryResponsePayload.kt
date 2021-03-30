package io.github.siyual_park.model.category

import java.time.Instant

data class CategoryResponsePayload(
    var id: String,

    var name: String,

    var createdAt: Instant,
    var updatedAt: Instant?
) {
    companion object {
        fun from(category: Category) = with(category) {
            CategoryResponsePayload(
                id!!,
                name,
                createdAt!!,
                updatedAt
            )
        }
    }
}
