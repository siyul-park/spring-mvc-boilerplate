package io.github.siyual_park.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.siyual_park.domain.security.TokenExchanger
import io.github.siyual_park.domain.user.UserCreateExecutor
import io.github.siyual_park.domain.user.UserCreatePayloadMapper
import io.github.siyual_park.expansion.readValue
import io.github.siyual_park.factory.UserCreatePayloadMockFactory
import io.github.siyual_park.model.token.TokenResponsePayload
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@ControllerTest
class TokenControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val userCreateExecutor: UserCreateExecutor,
    private val userCreatePayloadMapper: UserCreatePayloadMapper,
    private val tokenExchanger: TokenExchanger
) {

    private val userCreatePayloadMockFactory = UserCreatePayloadMockFactory()

    @Test
    fun testCreate() {
        val createUserPayload = userCreatePayloadMockFactory.create()
        val user = userCreatePayloadMapper.map(createUserPayload)
            .let { userCreateExecutor.execute(it) }

        val result = mockMvc.post("/token") {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("username", createUserPayload.name)
            param("password", createUserPayload.password)
        }
            .andExpect { status { isOk() } }
            .andReturn()

        val tokenPayload: TokenResponsePayload = objectMapper.readValue(result.response.contentAsString)
        assertEquals(tokenPayload.tokenType, "bearer")

        val token = tokenExchanger.decode(tokenPayload.accessToken)
        assertEquals(token.userId, user.id)
    }
}
