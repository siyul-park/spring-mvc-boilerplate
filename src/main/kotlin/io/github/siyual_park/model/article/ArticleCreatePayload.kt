package io.github.siyual_park.model.article

data class ArticleCreatePayload(
    var title: String,
    var content: String,
) {
    fun toArticle() = Article(title, content)
}
