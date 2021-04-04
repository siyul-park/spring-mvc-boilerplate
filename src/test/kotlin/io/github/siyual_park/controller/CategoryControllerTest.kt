package io.github.siyual_park.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.siyual_park.domain.article.ArticleCreatePayloadMapper
import io.github.siyual_park.domain.comment.CommentCreatePayloadMapper
import io.github.siyual_park.expansion.json
import io.github.siyual_park.expansion.readValue
import io.github.siyual_park.factory.ArticleCreatePayloadMockFactory
import io.github.siyual_park.factory.CategoryCreatePayloadMockFactory
import io.github.siyual_park.factory.CommentCreatePayloadMockFactory
import io.github.siyual_park.factory.RandomFactory
import io.github.siyual_park.model.category.CategoryResponsePayload
import io.github.siyual_park.model.category.CategoryUpdatePayload
import io.github.siyual_park.repository.ArticleRepository
import io.github.siyual_park.repository.CategoryRepository
import io.github.siyual_park.repository.CommentRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import java.time.Instant
import java.util.Optional
import kotlin.math.ceil

@ControllerTest
class CategoryControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val categoryRepository: CategoryRepository,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val articleCreatePayloadMapper: ArticleCreatePayloadMapper,
    private val commentCreatePayloadMapper: CommentCreatePayloadMapper
) {

    private val categoryCreatePayloadMockFactory = CategoryCreatePayloadMockFactory()

    @Test
    fun testCreate() {
        val payload = categoryCreatePayloadMockFactory.create()

        val result = mockMvc.post("/categories") { json(objectMapper.writeValueAsString(payload)) }
            .andExpect { status { isCreated() } }
            .andReturn()

        val category: CategoryResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertNotNull(category.id)
        assertEquals(category.name, payload.name)
        assertNotNull(category.createdAt)
        assertNotNull(category.updatedAt)
    }

    @Test
    fun testUpdate() {
        val created = categoryCreatePayloadMockFactory.create()
            .let { categoryRepository.create(it.toCategory()) }

        val payload = CategoryUpdatePayload(
            name = Optional.of(RandomFactory.createString(10))
        )
        val result = mockMvc.patch("/categories/${created.id}") { json(objectMapper.writeValueAsString(payload)) }
            .andExpect { status { isOk() } }
            .andReturn()

        val category: CategoryResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertNotNull(category.id)
        assertEquals(category.name, payload.name?.get())
        assertEquals(category.createdAt, created.createdAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
        assertEquals(category.updatedAt, created.updatedAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
    }

    @Test
    fun testFindById() {
        val created = categoryCreatePayloadMockFactory.create()
            .let { categoryRepository.create(it.toCategory()) }

        val result = mockMvc.get("/categories/${created.id}")
            .andExpect { status { isOk() } }
            .andReturn()

        val category: CategoryResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertEquals(category.id, created.id)
        assertEquals(category.name, created.name)
        assertEquals(category.createdAt, created.createdAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
        assertEquals(category.updatedAt, created.updatedAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
    }

    @Test
    fun testFindByName() {
        val created = categoryCreatePayloadMockFactory.create()
            .let { categoryRepository.create(it.toCategory()) }

        val result = mockMvc.get("/categories/@${created.name}")
            .andExpect { status { isOk() } }
            .andReturn()

        val category: CategoryResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertEquals(category.id, created.id)
        assertEquals(category.name, created.name)
        assertEquals(category.createdAt, created.createdAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
        assertEquals(category.updatedAt, created.updatedAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
    }

    @Test
    fun testFindAll() {
        categoryCreatePayloadMockFactory.create()
            .let { categoryRepository.create(it.toCategory()) }
        val count = categoryRepository.count()

        mockMvc.get("/categories")
            .andExpect {
                status { isOk() }
                header {
                    string("Total-Count", count.toString())
                    string("Total-Page", ceil(count / 20.0).toInt().toString())
                }
            }
            .andReturn()
    }

    @Test
    fun testDelete() {
        val created = categoryCreatePayloadMockFactory.create()
            .let { categoryRepository.create(it.toCategory()) }

        val articleCreatePayloadMockFactory = ArticleCreatePayloadMockFactory(created)
        val article = articleCreatePayloadMockFactory.create()
            .let { articleRepository.create(articleCreatePayloadMapper.map(it)) }

        val commentCreatePayloadMockFactory = CommentCreatePayloadMockFactory(article)
        val comment = commentCreatePayloadMockFactory.create()
            .let { commentRepository.create(commentCreatePayloadMapper.map(it)) }

        mockMvc.delete("/categories/${created.id}")
            .andExpect { status { isNoContent() } }
            .andReturn()

        assertFalse(created.id?.let { categoryRepository.existsById(it) } ?: false)
        assertFalse(article.id?.let { articleRepository.existsById(it) } ?: false)
        assertFalse(comment.id?.let { commentRepository.existsById(it) } ?: false)
    }
}
