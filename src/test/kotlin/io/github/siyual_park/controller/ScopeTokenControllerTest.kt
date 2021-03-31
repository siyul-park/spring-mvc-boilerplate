package io.github.siyual_park.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.siyual_park.domain.scope.ScopeTokenCreatePayloadMapper
import io.github.siyual_park.expansion.json
import io.github.siyual_park.expansion.readValue
import io.github.siyual_park.factory.ScopeTokenCreatePayloadMockFactory
import io.github.siyual_park.model.scope.ScopeTokenResponsePayload
import io.github.siyual_park.repository.ScopeTokenRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@ControllerTest
class ScopeTokenControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val scopeTokenRepository: ScopeTokenRepository,
    private val scopeTokenCreatePayloadMapper: ScopeTokenCreatePayloadMapper
) {

    private val scopeTokenCreatePayloadMockFactory = ScopeTokenCreatePayloadMockFactory()

    @Test
    fun testCreateSingle() {
        val payload = scopeTokenCreatePayloadMockFactory.create()

        val result = mockMvc.post("/scope-tokens") { json(objectMapper.writeValueAsString(payload)) }
            .andExpect { status { isCreated() } }
            .andReturn()

        val response: ScopeTokenResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertNotNull(response.id)
        assertEquals(response.name, payload.name)
        assertEquals(response.description, payload.description)
        assertTrue(response.children?.isEmpty() == true)
        assertNotNull(response.createdAt)
        assertNotNull(response.updatedAt)
    }

    @Test
    fun testCreateNest() {
        val child = scopeTokenCreatePayloadMockFactory.create()
            .let { scopeTokenCreatePayloadMapper.map(it) }
            .let { scopeTokenRepository.create(it) }

        val payload = scopeTokenCreatePayloadMockFactory.create()
        payload.children = setOf(child.id!!)

        val result = mockMvc.post("/scope-tokens") { json(objectMapper.writeValueAsString(payload)) }
            .andExpect { status { isCreated() } }
            .andReturn()

        val response: ScopeTokenResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertNotNull(response.id)
        assertEquals(response.name, payload.name)
        assertEquals(response.description, payload.description)
        assertTrue(response.children?.size == 1)
        assertNotNull(response.createdAt)
        assertNotNull(response.updatedAt)
    }
}
