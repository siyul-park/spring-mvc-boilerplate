package io.github.siyual_park.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.siyual_park.domain.user.UserCreateExecutor
import io.github.siyual_park.domain.user.UserCreatePayloadMapper
import io.github.siyual_park.expansion.authorization
import io.github.siyual_park.expansion.json
import io.github.siyual_park.expansion.readValue
import io.github.siyual_park.factory.AuthorizationFactory
import io.github.siyual_park.factory.RandomFactory
import io.github.siyual_park.factory.UserCreatePayloadMockFactory
import io.github.siyual_park.model.user.UserResponsePayload
import io.github.siyual_park.model.user.UserUpdatePayload
import io.github.siyual_park.repository.UserRepository
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
import java.util.Optional

@ControllerTest
class UserControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val authorizationFactory: AuthorizationFactory,
    private val userRepository: UserRepository,
    private val userCreateExecutor: UserCreateExecutor,
    private val userCreatePayloadMapper: UserCreatePayloadMapper,
    private val userCreatePayloadMockFactory: UserCreatePayloadMockFactory
) {

    @Test
    fun testCreate() {
        val payload = userCreatePayloadMockFactory.create()

        val result = mockMvc.post("/users") { json(objectMapper.writeValueAsString(payload)) }
            .andExpect { status { isCreated() } }
            .andReturn()

        val user: UserResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertNotNull(user.id)
        assertEquals(user.name, payload.name)
        assertEquals(user.nickname, payload.nickname)
        assertNotNull(user.createdAt)
        assertNotNull(user.updatedAt)
    }

    @Test
    fun testUpdate() {
        val created = userCreatePayloadMockFactory.create()
            .let { userCreatePayloadMapper.map(it) }
            .let { userCreateExecutor.execute(it) }

        val payload = UserUpdatePayload(
            nickname = Optional.of(RandomFactory.createString(10))
        )
        val result = mockMvc.patch("/users/${created.id}") {
            authorization(authorizationFactory.create(created))
            json(objectMapper.writeValueAsString(payload))
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val user: UserResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertNotNull(user.id)
        assertEquals(user.name, created.name)
        assertEquals(user.nickname, payload.nickname?.get())
        assertNotNull(user.createdAt)
        assertNotNull(user.updatedAt)
    }

    @Test
    fun testFindById() {
        val created = userCreatePayloadMockFactory.create()
            .let { userCreatePayloadMapper.map(it) }
            .let { userCreateExecutor.execute(it) }

        val result = mockMvc.get("/users/${created.id}") {
            authorization(authorizationFactory.create(created))
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val user: UserResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertEquals(user.id, created.id)
        assertEquals(user.name, created.name)
        assertEquals(user.nickname, created.nickname)
        assertNotNull(user.createdAt)
        assertNotNull(user.updatedAt)
    }

    @Test
    fun testFindByName() {
        val created = userCreatePayloadMockFactory.create()
            .let { userCreatePayloadMapper.map(it) }
            .let { userCreateExecutor.execute(it) }

        val result = mockMvc.get("/users/@${created.name}") {
            authorization(authorizationFactory.create(created))
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val user: UserResponsePayload = objectMapper.readValue(result.response.contentAsString)

        assertEquals(user.id, created.id)
        assertEquals(user.name, created.name)
        assertEquals(user.nickname, created.nickname)
        assertNotNull(user.createdAt)
        assertNotNull(user.updatedAt)
    }

    @Test
    fun testDelete() {
        val created = userCreatePayloadMockFactory.create()
            .let { userCreatePayloadMapper.map(it) }
            .let { userCreateExecutor.execute(it) }

        mockMvc.delete("/users/${created.id}") {
            authorization(authorizationFactory.create(created))
        }
            .andExpect { status { isNoContent() } }
            .andReturn()

        assertFalse(created.id?.let { userRepository.existsById(it) } ?: false)
    }
}
