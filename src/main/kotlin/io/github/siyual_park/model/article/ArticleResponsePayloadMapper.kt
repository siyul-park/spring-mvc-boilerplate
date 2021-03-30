package io.github.siyual_park.model.article

import io.github.siyual_park.model.Mapper
import org.springframework.stereotype.Component

@Component
class ArticleResponsePayloadMapper : Mapper<Article, ArticleResponsePayload> {
    override fun map(input: Article) = ArticleResponsePayload.from(input)
}
