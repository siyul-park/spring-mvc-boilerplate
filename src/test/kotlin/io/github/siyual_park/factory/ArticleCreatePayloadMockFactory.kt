package io.github.siyual_park.factory

import io.github.siyual_park.model.article.ArticleCreatePayload

object ArticleCreatePayloadMockFactory : MockFactory<ArticleCreatePayload> {
    private var count: Int = 0

    override fun create(): ArticleCreatePayload {
        val count = count++
        return ArticleCreatePayload(
            "${RandomFactory.createString(10)}-$count",
            "${RandomFactory.createString(10)}-$count",
        )
    }
}
