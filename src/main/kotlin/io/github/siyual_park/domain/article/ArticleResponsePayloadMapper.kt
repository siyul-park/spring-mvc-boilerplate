package io.github.siyual_park.domain.article

import io.github.siyual_park.domain.Mapper
import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.article.ArticleResponsePayload
import org.springframework.stereotype.Component

@Component
class ArticleResponsePayloadMapper : Mapper<Article, ArticleResponsePayload> {
    override fun map(input: Article) = ArticleResponsePayload.from(input)
}
