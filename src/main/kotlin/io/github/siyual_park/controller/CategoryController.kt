package io.github.siyual_park.controller

import io.github.siyual_park.config.PreDefinedScope
import io.github.siyual_park.domain.Paginator
import io.github.siyual_park.domain.category.CategoryDeleteExecutor
import io.github.siyual_park.domain.category.CategoryResponsePayloadMapper
import io.github.siyual_park.exception.AccessDeniedException
import io.github.siyual_park.model.article.ArticleUpdatePayload
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.model.category.CategoryCreatePayload
import io.github.siyual_park.model.category.CategoryResponsePayload
import io.github.siyual_park.model.user.User
import io.github.siyual_park.repository.CategoryRepository
import io.github.siyual_park.repository.patch.JsonMergePatchFactory
import io.swagger.annotations.Api
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
import javax.persistence.LockModeType
import javax.transaction.Transactional

@Api
@RestController
@RequestMapping("/categories")
class CategoryController(
    private val categoryRepository: CategoryRepository,
    private val categoryResponsePayloadMapper: CategoryResponsePayloadMapper,
    private val categoryDeleteExecutor: CategoryDeleteExecutor,
    private val jsonMergePatchFactory: JsonMergePatchFactory
) {

    private val paginator = Paginator.of(categoryRepository, categoryResponsePayloadMapper)

    @PreAuthorize("hasAuthority('${PreDefinedScope.Category.create}')")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @AuthenticationPrincipal user: User,
        @RequestBody payload: CategoryCreatePayload
    ): CategoryResponsePayload {
        return categoryRepository.create(Category(payload.name, user))
            .let { categoryResponsePayloadMapper.map(it) }
    }

    @Transactional
    @PreAuthorize("hasAuthority('${PreDefinedScope.Category.update}')")
    @PatchMapping("/{category-id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @AuthenticationPrincipal user: User,
        @PathVariable("category-id") id: String,
        @RequestBody payload: ArticleUpdatePayload
    ): CategoryResponsePayload {
        val category = categoryRepository.findByIdOrFail(id, LockModeType.PESSIMISTIC_WRITE)
        if (category.owner.id != user.id) {
            throw AccessDeniedException()
        }
        return categoryRepository.update(category, jsonMergePatchFactory.create(payload))
            .let { categoryResponsePayloadMapper.map(it) }
    }

    @PreAuthorize("hasAuthority('${PreDefinedScope.Category.read}')")
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

    @PreAuthorize("hasAuthority('${PreDefinedScope.Category.read}')")
    @GetMapping("/{category-id}")
    @ResponseStatus(HttpStatus.OK)
    fun findById(@PathVariable("category-id") id: String): CategoryResponsePayload {
        return categoryRepository.findByIdOrFail(id)
            .let { categoryResponsePayloadMapper.map(it) }
    }

    @PreAuthorize("hasAuthority('${PreDefinedScope.Category.read}')")
    @GetMapping("/@{category-name}")
    @ResponseStatus(HttpStatus.OK)
    fun findByName(@PathVariable("category-name") name: String): CategoryResponsePayload {
        return categoryRepository.findByNameOrFail(name)
            .let { categoryResponsePayloadMapper.map(it) }
    }

    @Transactional
    @PreAuthorize("hasAuthority('${PreDefinedScope.Category.delete}')")
    @DeleteMapping("/{category-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal user: User,
        @PathVariable("category-id") id: String
    ) {
        val category = categoryRepository.findByIdOrFail(id, LockModeType.PESSIMISTIC_WRITE)
        if (category.owner.id != user.id) {
            throw AccessDeniedException()
        }
        categoryDeleteExecutor.execute(category)
    }
}
