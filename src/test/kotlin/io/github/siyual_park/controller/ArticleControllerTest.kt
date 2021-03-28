package io.github.siyual_park.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.siyual_park.expansion.json
import io.github.siyual_park.expansion.readValue
import io.github.siyual_park.factory.ArticleCreatePayloadMockFactory
import io.github.siyual_park.factory.RandomFactory
import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.article.ArticleUpdatePayload
import io.github.siyual_park.repository.ArticleRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import java.util.Optional

@ControllerTest
class ArticleControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val articleRepository: ArticleRepository
) {

    @Test
    fun testCreateArticle() {
        val payload = ArticleCreatePayloadMockFactory.create()

        val result = mockMvc.post("/articles") { json(objectMapper.writeValueAsString(payload)) }
            .andExpect { status { isCreated() } }
            .andReturn()

        val article: Article = objectMapper.readValue(result.response.contentAsString)

        assertNotNull(article.id)
        assertEquals(article.title, payload.title)
        assertEquals(article.content, payload.content)
        assertNotNull(article.createdAt)
        assertNotNull(article.updatedAt)
    }

    @Test
    fun testUpdateArticle() {
        val created = ArticleCreatePayloadMockFactory.create()
            .let { articleRepository.save(it.toArticle()) }

        val titleUpdatePayload = ArticleUpdatePayload(
            Optional.of(RandomFactory.createString(10)),
            null
        )
        val result = mockMvc.patch("/articles/${created.id}") { json(objectMapper.writeValueAsString(titleUpdatePayload)) }
            .andExpect { status { isOk() } }
            .andReturn()

        val article: Article = objectMapper.readValue(result.response.contentAsString)

        assertNotNull(article.id)
        assertEquals(article.title, titleUpdatePayload.title?.get())
        assertEquals(article.content, created.content)
        assertNotNull(article.createdAt)
        assertNotNull(article.updatedAt)
    }
}
