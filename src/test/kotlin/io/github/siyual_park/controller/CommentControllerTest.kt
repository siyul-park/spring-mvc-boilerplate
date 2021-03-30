package io.github.siyual_park.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.siyual_park.expansion.json
import io.github.siyual_park.expansion.readValue
import io.github.siyual_park.factory.ArticleCreatePayloadMockFactory
import io.github.siyual_park.factory.CommentCreatePayloadMockFactory
import io.github.siyual_park.factory.RandomFactory
import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.article.ArticleCreatePayloadMapper
import io.github.siyual_park.model.category.Category
import io.github.siyual_park.model.comment.CommentCreatePayloadMapper
import io.github.siyual_park.model.comment.CommentResponsePayload
import io.github.siyual_park.model.comment.CommentUpdatePayload
import io.github.siyual_park.repository.ArticleRepository
import io.github.siyual_park.repository.CategoryRepository
import io.github.siyual_park.repository.CommentRepository
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
import java.util.Optional

@ControllerTest
class CommentControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val commentRepository: CommentRepository,
    private val articleRepository: ArticleRepository,
    private val categoryRepository: CategoryRepository,
    private val commentCreatePayloadMapper: CommentCreatePayloadMapper,
    private val articleCreatePayloadMapper: ArticleCreatePayloadMapper
) {

    private lateinit var category: Category
    private lateinit var article: Article
    private lateinit var articleCreatePayloadMockFactory: ArticleCreatePayloadMockFactory
    private lateinit var commentCreatePayloadMockFactory: CommentCreatePayloadMockFactory

    @BeforeEach
    fun prepare() {
        category = categoryRepository.create(Category(RandomFactory.createString(10)))
        articleCreatePayloadMockFactory = ArticleCreatePayloadMockFactory(category)

        article = articleCreatePayloadMockFactory.create()
            .let { articleCreatePayloadMapper.map(it) }
            .let { articleRepository.create(it) }
        commentCreatePayloadMockFactory = CommentCreatePayloadMockFactory(article)
    }

    @Test
    fun testCreate() {
        val payload = commentCreatePayloadMockFactory.create()

        val result = mockMvc.post("/comments") { json(objectMapper.writeValueAsString(payload)) }
            .andExpect { status { isCreated() } }
            .andReturn()

        val comment: CommentResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertNotNull(comment.id)
        assertEquals(comment.content, payload.content)
        assertEquals(comment.contentType, payload.contentType)
        assertNotNull(comment.articleId)
        assertNotNull(comment.updatedAt)
    }

    @Test
    fun testUpdate() {
        val created = commentCreatePayloadMockFactory.create()
            .let { commentCreatePayloadMapper.map(it) }
            .let { commentRepository.create(it) }

        val payload = CommentUpdatePayload(
            content = Optional.of(RandomFactory.createString(10))
        )
        val result = mockMvc.patch("/comments/${created.id}") { json(objectMapper.writeValueAsString(payload)) }
            .andExpect { status { isOk() } }
            .andReturn()

        val comment: CommentResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertNotNull(comment.id)
        assertEquals(comment.content, payload.content?.get())
        assertEquals(comment.contentType, created.contentType)
        assertNotNull(comment.articleId)
        assertNotNull(comment.updatedAt)
    }

    @Test
    fun testFindById() {
        val created = commentCreatePayloadMockFactory.create()
            .let { commentCreatePayloadMapper.map(it) }
            .let { commentRepository.create(it) }

        val result = mockMvc.get("/comments/${created.id}")
            .andExpect { status { isOk() } }
            .andReturn()

        val comment: CommentResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertEquals(comment.id, created.id)
        assertEquals(comment.content, created.content)
        assertEquals(comment.contentType, created.contentType)
        assertEquals(comment.articleId, created.article.id)
        assertNotNull(comment.updatedAt)
        assertNotNull(comment.createdAt)
    }

    @Test
    fun testDelete() {
        val created = commentCreatePayloadMockFactory.create()
            .let { commentCreatePayloadMapper.map(it) }
            .let { commentRepository.create(it) }

        mockMvc.delete("/comments/${created.id}")
            .andExpect { status { isNoContent() } }
            .andReturn()

        assertFalse(created.id?.let { commentRepository.existsById(it) } ?: false)
    }
}
