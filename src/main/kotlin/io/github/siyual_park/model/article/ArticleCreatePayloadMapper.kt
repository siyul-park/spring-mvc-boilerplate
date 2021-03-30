package io.github.siyual_park.model.article

import io.github.siyual_park.model.Mapper
import io.github.siyual_park.repository.CachedCategoryRepository

class ArticleCreatePayloadMapper(
    private val categoryRepository: CachedCategoryRepository
) : Mapper<ArticleCreatePayload, Article> {
    override fun map(input: ArticleCreatePayload): Article {
        val category = categoryRepository.findByIdOrFail(input.categoryId)
        return Article(input.title, input.content, input.contentType, category)
    }
}
