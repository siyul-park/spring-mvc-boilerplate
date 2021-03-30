package io.github.siyual_park.model

import com.google.common.base.CaseFormat
import java.lang.Exception
import java.net.URI

data class ErrorResponse(
    val error: String,
    val errorDescription: String? = null,
    val errorUri: URI? = null
) {
    companion object {
        fun from(exception: Exception) = ErrorResponse(
            CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, exception.javaClass.simpleName),
            exception.message
        )
    }
}
