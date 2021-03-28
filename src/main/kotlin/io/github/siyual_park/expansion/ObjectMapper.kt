package io.github.siyual_park.expansion

import com.fasterxml.jackson.databind.ObjectMapper

inline fun <reified T> ObjectMapper.readValue(content: String): T = readValue(content, T::class.java)
