package io.github.siyual_park.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.siyual_park.expansion.json
import io.github.siyual_park.expansion.readValue
import io.github.siyual_park.model.article.Article
import io.github.siyual_park.model.article.ArticleCreatePayload
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@ControllerTest
class ArticleControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun testCreateArticle() {
        val payload = ArticleCreatePayload(
            "test",
            "test"
        )

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
}
