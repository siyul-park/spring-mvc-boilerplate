package io.github.siyual_park.expansion

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockHttpServletRequestDsl

fun MockHttpServletRequestDsl.json(value: String) {
    contentType = MediaType.APPLICATION_JSON
    content = value
}

fun MockHttpServletRequestDsl.authorization(value: String) {
    header("Authorization", value)
}
