package io.github.siyual_park.controller

import io.github.siyual_park.domain.ArticlePatchFactory
import io.github.siyual_park.domain.Paginator
import io.github.siyual_park.model.article.ArticleCreatePayload
import io.github.siyual_park.model.article.ArticleCreatePayloadMapper
import io.github.siyual_park.model.article.ArticleResponsePayload
import io.github.siyual_park.model.article.ArticleResponsePayloadMapper
import io.github.siyual_park.model.article.ArticleUpdatePayload
import io.github.siyual_park.repository.ArticleRepository
import io.github.siyual_park.repository.CategoryRepository
import io.github.siyual_park.repository.patch.LambdaPatch
import io.swagger.annotations.Api
import org.springframework.data.domain.Sort
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
    categoryRepository: CategoryRepository,
    private val articlePatchFactory: ArticlePatchFactory
) {
    private val articleCreatePayloadMapper = ArticleCreatePayloadMapper(categoryRepository)
    private val articleResponsePayloadMapper = ArticleResponsePayloadMapper()

    private val paginator = Paginator.of(articleRepository, articleResponsePayloadMapper)

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody payload: ArticleCreatePayload): ArticleResponsePayload {
        return articleRepository.create(articleCreatePayloadMapper.map(payload))
            .let { articleResponsePayloadMapper.map(it) }
    }

    @PatchMapping("/{article-id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable("article-id") id: String,
        @RequestBody payload: ArticleUpdatePayload
    ): ArticleResponsePayload {
        return articleRepository.updateById(id, articlePatchFactory.create(payload))
            .let { articleResponsePayloadMapper.map(it) }
    }

    @GetMapping("")
    fun findAll(
        @RequestParam("page", required = false) offset: Int?,
        @RequestParam("per_page", required = false) limit: Int?,
        @RequestParam("sort", required = false) property: String?,
        @RequestParam("order", required = false) direction: Sort.Direction?
    ): ResponseEntity<Stream<ArticleResponsePayload>> {
        return paginator.query(
            offset = offset,
            limit = limit,
            sort = createSort(property, direction)
        )
    }

    @GetMapping("/{article-id}")
    @ResponseStatus(HttpStatus.OK)
    fun find(@PathVariable("article-id") id: String): ArticleResponsePayload {
        return articleRepository.updateById(id, LambdaPatch.from { views += 1 })
            .let { articleResponsePayloadMapper.map(it) }
    }

    @DeleteMapping("/{article-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("article-id") id: String) {
        return articleRepository.deleteById(id)
    }

    private fun createSort(
        property: String?,
        direction: Sort.Direction?
    ) = Sort.by(
        direction ?: Sort.Direction.ASC,
        property ?: "id"
    )
}
