package io.github.siyual_park.controller

import io.github.siyual_park.domain.Paginator
import io.github.siyual_park.model.article.ArticleUpdatePayload
import io.github.siyual_park.model.category.CategoryCreatePayload
import io.github.siyual_park.model.category.CategoryResponsePayload
import io.github.siyual_park.model.category.CategoryResponsePayloadMapper
import io.github.siyual_park.repository.ArticleRepository
import io.github.siyual_park.repository.CategoryRepository
import io.github.siyual_park.repository.patch.JsonMergePatchFactory
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
import javax.transaction.Transactional

@Api
@RestController
@RequestMapping("/categories")
class CategoryController(
    private val categoryRepository: CategoryRepository,
    private val articleRepository: ArticleRepository,
    private val jsonMergePatchFactory: JsonMergePatchFactory
) {

    private val categoryResponsePayloadMapper = CategoryResponsePayloadMapper()
    private val paginator = Paginator.of(categoryRepository, categoryResponsePayloadMapper)

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody payload: CategoryCreatePayload): CategoryResponsePayload {
        return categoryRepository.create(payload.toCategory())
            .let { categoryResponsePayloadMapper.map(it) }
    }

    @PatchMapping("/{category-id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable("category-id") id: String,
        @RequestBody payload: ArticleUpdatePayload
    ): CategoryResponsePayload {
        return categoryRepository.updateById(id, jsonMergePatchFactory.create(payload))
            .let { categoryResponsePayloadMapper.map(it) }
    }

    @GetMapping("")
    fun findAll(
        @RequestParam("page", required = false) offset: Int?,
        @RequestParam("per_page", required = false) limit: Int?,
        @RequestParam("sort", required = false) property: String?,
        @RequestParam("order", required = false) direction: Sort.Direction?
    ): ResponseEntity<Stream<CategoryResponsePayload>> {
        return paginator.query(
            offset = offset,
            limit = limit,
            sort = Sort.by(
                direction ?: Sort.Direction.ASC,
                property ?: "id"
            )
        )
    }

    @GetMapping("/{category-id}")
    @ResponseStatus(HttpStatus.OK)
    fun findById(@PathVariable("category-id") id: String): CategoryResponsePayload {
        return categoryRepository.findByIdOrFail(id)
            .let { categoryResponsePayloadMapper.map(it) }
    }

    @GetMapping("/@{category-name}")
    @ResponseStatus(HttpStatus.OK)
    fun findByName(@PathVariable("category-name") name: String): CategoryResponsePayload {
        return categoryRepository.findByNameOrFail(name)
            .let { categoryResponsePayloadMapper.map(it) }
    }

    @DeleteMapping("/{category-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    fun delete(@PathVariable("category-id") id: String) {
        articleRepository.deleteAllByCategory(id)
        return categoryRepository.deleteById(id)
    }
}
