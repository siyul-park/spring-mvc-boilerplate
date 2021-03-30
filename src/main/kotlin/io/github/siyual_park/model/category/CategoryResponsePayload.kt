package io.github.siyual_park.model.category

import java.time.Instant

data class CategoryResponsePayload(
    val id: String,

    var name: String,

    val createdAt: Instant,
    val updatedAt: Instant?
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
