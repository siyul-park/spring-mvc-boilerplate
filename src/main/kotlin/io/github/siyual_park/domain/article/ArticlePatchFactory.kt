package io.github.siyual_park.domain.article

import io.github.siyual_park.exception.BadRequestException
import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.article.ArticleUpdatePayload
import io.github.siyual_park.repository.CachedCategoryRepository
import io.github.siyual_park.repository.patch.JsonMergePatchFactory
import io.github.siyual_park.repository.patch.LambdaPatch
import io.github.siyual_park.repository.patch.Patch
import org.springframework.stereotype.Component

@Component
class ArticlePatchFactory(
    private val categoryRepository: CachedCategoryRepository,
    private val jsonMergePatchFactory: JsonMergePatchFactory
) {
    fun create(payload: ArticleUpdatePayload): Patch<Article> {
        val jsonMergePatch: Patch<Article> = jsonMergePatchFactory.create(payload)
        return LambdaPatch.from {
            payload.categoryId?.let {
                if (it.isPresent) {
                    category = categoryRepository.findByIdOrFail(it.get())
                } else {
                    throw BadRequestException()
                }
                payload.categoryId = null
            }

            jsonMergePatch.apply(this)
        }
    }
}
