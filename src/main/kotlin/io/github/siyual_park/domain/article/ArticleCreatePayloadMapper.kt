package io.github.siyual_park.domain.article

import io.github.siyual_park.model.Mapper
import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.article.ArticleCreatePayload
import io.github.siyual_park.repository.CachedCategoryRepository
import org.springframework.stereotype.Component

@Component
class ArticleCreatePayloadMapper(
    private val categoryRepository: CachedCategoryRepository
) : Mapper<ArticleCreatePayload, Article> {
    override fun map(input: ArticleCreatePayload): Article {
        val category = categoryRepository.findByIdOrFail(input.categoryId)
        return Article(input.title, input.content, input.contentType, category)
    }
}
