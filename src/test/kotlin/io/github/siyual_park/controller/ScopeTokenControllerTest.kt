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
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.Instant
import kotlin.math.ceil

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

    @Test
    fun testFindById() {
        val created = scopeTokenCreatePayloadMockFactory.create()
            .let { scopeTokenCreatePayloadMapper.map(it) }
            .let { scopeTokenRepository.create(it) }

        val result = mockMvc.get("/scope-tokens/${created.id}")
            .andExpect { status { isOk() } }
            .andReturn()

        val response: ScopeTokenResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertEquals(response.id, created.id)
        assertEquals(response.name, created.name)
        assertEquals(response.description, created.description)
        assertTrue(response.children?.isEmpty() == true)
        assertEquals(response.createdAt, created.createdAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
        assertEquals(response.updatedAt, created.updatedAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
    }

    @Test
    fun testFindByName() {
        val created = scopeTokenCreatePayloadMockFactory.create()
            .let { scopeTokenCreatePayloadMapper.map(it) }
            .let { scopeTokenRepository.create(it) }

        val result = mockMvc.get("/scope-tokens/@${created.name}")
            .andExpect { status { isOk() } }
            .andReturn()

        val response: ScopeTokenResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertEquals(response.id, created.id)
        assertEquals(response.name, created.name)
        assertEquals(response.description, created.description)
        assertTrue(response.children?.isEmpty() == true)
        assertEquals(response.createdAt, created.createdAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
        assertEquals(response.updatedAt, created.updatedAt?.epochSecond?.let { Instant.ofEpochSecond(it) })
    }

    @Test
    fun testFindByAll() {
        scopeTokenCreatePayloadMockFactory.create()
            .let { scopeTokenCreatePayloadMapper.map(it) }
            .let { scopeTokenRepository.create(it) }
        val count = scopeTokenRepository.count()

        mockMvc.get("/scope-tokens")
            .andExpect {
                status { isOk() }
                header {
                    string("Total-Count", count.toString())
                    string("Total-Page", ceil(count / 20.0).toInt().toString())
                }
            }
            .andReturn()
    }
}
