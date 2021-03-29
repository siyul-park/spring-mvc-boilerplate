package io.github.siyual_park.controller

import io.github.siyual_park.model.category.Category
import io.github.siyual_park.model.category.CategoryCreatePayload
import io.github.siyual_park.repository.CategoryRepository
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Api
@RestController
@RequestMapping("/categories")
class CategoryController(
    private val categoryRepository: CategoryRepository
) {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody payload: CategoryCreatePayload): Category {
        return categoryRepository.create(payload.toCategory())
    }
}
