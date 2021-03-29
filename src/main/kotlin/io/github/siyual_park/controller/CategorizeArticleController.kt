package io.github.siyual_park.controller

import io.github.siyual_park.domain.Paginator
import io.github.siyual_park.model.article.Article
import io.github.siyual_park.repository.ArticleRepository
import io.github.siyual_park.repository.CategoryRepository
import io.github.siyual_park.repository.specification.ArticleSpecification
import io.swagger.annotations.Api
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.support.JdbcUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Stream

@Api
@RestController
@RequestMapping("/categories")
class CategorizeArticleController(
    articleRepository: ArticleRepository,
    private val categoryRepository: CategoryRepository,
) {

    private val paginator = Paginator(articleRepository)

    @GetMapping("/{category-id}/articles")
    fun findAllById(
        @PathVariable("category-id") id: String,
        @RequestParam("page", required = false) offset: Int?,
        @RequestParam("per_page", required = false) limit: Int?,
        @RequestParam("sort", required = false) property: String?,
        @RequestParam("order", required = false) direction: Sort.Direction?
    ): ResponseEntity<Stream<Article>> {
        val category = categoryRepository.findByIdOrFail(id)

        return paginator.query(
            offset = offset,
            limit = limit,
            sort = createSort(property, direction),
            spec = ArticleSpecification.withCategory(category.id!!)
        )
    }

    @GetMapping("/@{category-name}/articles")
    fun findAllByName(
        @PathVariable("category-name") name: String,
        @RequestParam("page", required = false) offset: Int?,
        @RequestParam("per_page", required = false) limit: Int?,
        @RequestParam("sort", required = false) property: String?,
        @RequestParam("order", required = false) direction: Sort.Direction?
    ): ResponseEntity<Stream<Article>> {
        val category = categoryRepository.findByNameOrFail(name)

        return paginator.query(
            offset = offset,
            limit = limit,
            sort = createSort(property, direction),
            spec = ArticleSpecification.withCategory(category.id!!)
        )
    }

    private fun createSort(
        property: String?,
        direction: Sort.Direction?
    ) = Sort.by(
        direction ?: Sort.Direction.ASC,
        JdbcUtils.convertUnderscoreNameToPropertyName(property ?: "id")
    )
}
