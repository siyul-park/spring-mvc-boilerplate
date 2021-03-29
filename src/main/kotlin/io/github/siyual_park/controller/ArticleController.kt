package io.github.siyual_park.controller

import io.github.siyual_park.domain.Paginator
import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.article.ArticleCreatePayload
import io.github.siyual_park.model.article.ArticleUpdatePayload
import io.github.siyual_park.repository.ArticleRepository
import io.github.siyual_park.repository.patch.JsonMergePatchFactory
import io.github.siyual_park.repository.patch.LambdaPatch
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Stream

@Api
@RestController
@RequestMapping("/articles")
class ArticleController(
    private val articleRepository: ArticleRepository,
    private val jsonMergePatchFactory: JsonMergePatchFactory
) {

    private val paginator = Paginator(articleRepository)

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody payload: ArticleCreatePayload): Article {
        return articleRepository.create(payload.toArticle())
    }

    @PatchMapping("/{article-id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable("article-id") id: String,
        @RequestBody payload: ArticleUpdatePayload
    ): Article {
        return articleRepository.updateByIdOrFail(id, jsonMergePatchFactory.create(payload))
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun findAll(
        @RequestParam("page", required = false) page: Int?,
        @RequestParam("per_page", required = false) perPage: Int?
    ): ResponseEntity<Stream<Article>> {
        return paginator.query(page, perPage)
    }

    @GetMapping("/{article-id}")
    @ResponseStatus(HttpStatus.OK)
    fun find(@PathVariable("article-id") id: String): Article {
        return articleRepository.updateByIdOrFail(id, LambdaPatch.from { views += 1 })
    }

    @DeleteMapping("/{article-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("article-id") id: String) {
        return articleRepository.deleteByIdOrFail(id)
    }
}
