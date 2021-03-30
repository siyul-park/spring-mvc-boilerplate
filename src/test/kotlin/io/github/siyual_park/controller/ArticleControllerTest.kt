package io.github.siyual_park.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.siyual_park.expansion.json
import io.github.siyual_park.expansion.readValue
import io.github.siyual_park.factory.ArticleCreatePayloadMockFactory
import io.github.siyual_park.factory.RandomFactory
import io.github.siyual_park.model.article.ArticleCreatePayloadMapper
import io.github.siyual_park.model.article.ArticleResponsePayload
import io.github.siyual_park.model.article.ArticleUpdatePayload
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.repository.ArticleRepository
import io.github.siyual_park.repository.CategoryRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
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
class ArticleControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val articleRepository: ArticleRepository,
    private val categoryRepository: CategoryRepository,
    private val articleCreatePayloadMapper: ArticleCreatePayloadMapper
) {

    private lateinit var category: Category
    private lateinit var articleCreatePayloadMockFactory: ArticleCreatePayloadMockFactory

    @BeforeEach
    fun prepare() {
        category = categoryRepository.create(Category(RandomFactory.createString(10)))
        articleCreatePayloadMockFactory = ArticleCreatePayloadMockFactory(category)
    }

    @Test
    fun testCreate() {
        val payload = articleCreatePayloadMockFactory.create()

        val result = mockMvc.post("/articles") { json(objectMapper.writeValueAsString(payload)) }
            .andExpect { status { isCreated() } }
            .andReturn()

        val article: ArticleResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertNotNull(article.id)
        assertEquals(article.title, payload.title)
        assertEquals(article.content, payload.content)
        assertEquals(article.contentType, payload.contentType)
        assertNotNull(article.createdAt)
        assertNotNull(article.updatedAt)
    }

    @Test
    fun testUpdate() {
        val created = articleCreatePayloadMockFactory.create()
            .let { articleRepository.create(articleCreatePayloadMapper.map(it)) }

        val titleUpdatePayload = ArticleUpdatePayload(
            title = Optional.of(RandomFactory.createString(10))
        )
        val result = mockMvc.patch("/articles/${created.id}") { json(objectMapper.writeValueAsString(titleUpdatePayload)) }
            .andExpect { status { isOk() } }
            .andReturn()

        val article: ArticleResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertEquals(article.id, created.id)
        assertEquals(article.title, titleUpdatePayload.title?.get())
        assertEquals(article.content, created.content)
        assertEquals(article.contentType, created.contentType)
        assertEquals(article.createdAt, created.createdAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
        assertEquals(article.updatedAt, created.updatedAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
    }

    @Test
    fun testFind() {
        val created = articleCreatePayloadMockFactory.create()
            .let { articleRepository.create(articleCreatePayloadMapper.map(it)) }

        val result = mockMvc.get("/articles/${created.id}")
            .andExpect { status { isOk() } }
            .andReturn()

        val article: ArticleResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertEquals(article.id, created.id)
        assertEquals(article.title, created.title)
        assertEquals(article.content, created.content)
        assertEquals(article.contentType, created.contentType)
        assertEquals(article.createdAt, created.createdAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
        assertEquals(article.updatedAt, created.updatedAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
    }

    @Test
    fun testFindAll() {
        articleCreatePayloadMockFactory.create()
            .let { articleRepository.create(articleCreatePayloadMapper.map(it)) }
        val count = articleRepository.count()

        mockMvc.get("/articles")
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
        val created = articleCreatePayloadMockFactory.create()
            .let { articleRepository.create(articleCreatePayloadMapper.map(it)) }

        mockMvc.delete("/articles/${created.id}")
            .andExpect { status { isNoContent() } }
            .andReturn()

        assertFalse(created.id?.let { articleRepository.existsById(it) } ?: false)
    }
}
