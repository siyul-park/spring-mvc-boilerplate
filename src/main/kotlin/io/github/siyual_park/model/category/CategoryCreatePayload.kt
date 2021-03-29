package io.github.siyual_park.model.category

data class CategoryCreatePayload(
    var name: String,
) {
    fun toCategory() = Category(name)
}
