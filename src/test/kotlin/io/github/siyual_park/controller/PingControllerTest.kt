package io.github.siyual_park.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@ControllerTest
class PingControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
) {

    @Test
    fun testPing() {
        mockMvc.get("/ping")
            .andExpect {
                status { isOk() }
                content { string("pong") }
            }
    }
}
