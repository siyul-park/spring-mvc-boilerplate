package io.github.siyual_park.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.jcabi.manifests.Manifests
import io.github.siyual_park.expansion.readValue
import io.github.siyual_park.model.Version
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@ControllerTest
class VersionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun testGetVersion() {
        val result = mockMvc.get("/version")
            .andExpect {
                status { isOk() }
            }.andReturn()

        val version: Version = objectMapper.readValue(result.response.contentAsString)

        assertEquals(version.version, javaClass.`package`.specificationVersion)
        assertEquals(version.commit, javaClass.`package`.implementationVersion)
        assertEquals(version.buildTime, Manifests.DEFAULT["Build-Time"])
    }
}
