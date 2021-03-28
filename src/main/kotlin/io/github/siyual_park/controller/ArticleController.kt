package io.github.siyual_park.controller

import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.article.ArticleCreatePayload
import io.github.siyual_park.model.article.ArticleUpdatePayload
import io.github.siyual_park.repository.ArticleRepository
import io.github.siyual_park.repository.patch.JsonMergePatchFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/articles")
class ArticleController(
    private val articleRepository: ArticleRepository,
    private val jsonMergePatchFactory: JsonMergePatchFactory
) {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody payload: ArticleCreatePayload): Article {
        return articleRepository.save(payload.toArticle())
    }

    @PatchMapping("/{article_id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable("article_id") id: String,
        @RequestBody payload: ArticleUpdatePayload
    ): Article {
        return articleRepository.updateByIdOrFail(id, jsonMergePatchFactory.create(payload))
    }
}
