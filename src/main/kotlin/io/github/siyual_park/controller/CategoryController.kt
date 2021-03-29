package io.github.siyual_park.controller

import io.github.siyual_park.domain.Paginator
import io.github.siyual_park.model.article.ArticleUpdatePayload
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.model.category.CategoryCreatePayload
import io.github.siyual_park.repository.CategoryRepository
import io.github.siyual_park.repository.patch.JsonMergePatchFactory
import io.swagger.annotations.Api
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.support.JdbcUtils
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
@RequestMapping("/categories")
class CategoryController(
    private val categoryRepository: CategoryRepository,
    private val jsonMergePatchFactory: JsonMergePatchFactory
) {

    private val paginator = Paginator(categoryRepository)

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody payload: CategoryCreatePayload): Category {
        return categoryRepository.create(payload.toCategory())
    }

    @PatchMapping("/{category-id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable("category-id") id: String,
        @RequestBody payload: ArticleUpdatePayload
    ): Category {
        return categoryRepository.updateByIdOrFail(id, jsonMergePatchFactory.create(payload))
    }

    @GetMapping("")
    fun findAll(
        @RequestParam("page", required = false) offset: Int?,
        @RequestParam("per_page", required = false) limit: Int?,
        @RequestParam("sort", required = false) property: String?,
        @RequestParam("order", required = false) direction: Sort.Direction?
    ): ResponseEntity<Stream<Category>> {
        return paginator.query(
            offset = offset,
            limit = limit,
            sort = createSort(property, direction)
        )
    }

    @GetMapping("/{category-id}")
    @ResponseStatus(HttpStatus.OK)
    fun findById(@PathVariable("category-id") id: String): Category {
        return categoryRepository.findByIdOrFail(id)
    }

    @GetMapping("/@{category-name}")
    @ResponseStatus(HttpStatus.OK)
    fun findByName(@PathVariable("category-name") name: String): Category {
        return categoryRepository.findByNameOrFail(name)
    }

    @DeleteMapping("/{category-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("category-id") id: String) {
        return categoryRepository.deleteByIdOrFail(id)
    }

    private fun createSort(
        property: String?,
        direction: Sort.Direction?
    ) = Sort.by(
        direction ?: Sort.Direction.ASC,
        JdbcUtils.convertUnderscoreNameToPropertyName(property ?: "id")
    )
}
