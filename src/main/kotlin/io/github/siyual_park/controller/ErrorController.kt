package io.github.siyual_park.controller

import com.google.common.base.CaseFormat
import io.github.siyual_park.model.ErrorResponse
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.util.Collections
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("\${server.error.path:\${error.path:/error}}")
class ErrorController(
    errorAttributes: ErrorAttributes
) : AbstractErrorController(errorAttributes) {
    @RequestMapping(produces = [MediaType.TEXT_HTML_VALUE])
    fun errorHtml(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        val status = getStatus(request)
        var model = Collections
            .unmodifiableMap(
                getErrorAttributes(
                    request,
                    getErrorAttributeOptions()
                )
            )
        response.status = status.value()
        if (status.is5xxServerError) {
            model = mapOf("message" to "Internal Server Error")
        }

        val modelAndView = resolveErrorView(request, response, status, model)
        return modelAndView ?: ModelAndView("error", model)
    }

    @RequestMapping
    fun error(request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val status = getStatus(request)
        if (status == HttpStatus.NO_CONTENT) {
            return ResponseEntity(status)
        }

        val body: Map<String, Any> = getErrorAttributes(request, getErrorAttributeOptions())
        return ResponseEntity(
            ErrorResponse(
                error = CaseFormat.UPPER_CAMEL.to(
                    CaseFormat.LOWER_UNDERSCORE,
                    if (status.is5xxServerError) {
                        "InternalServerError"
                    } else {
                        body["error"] as? String ?: ""
                    }
                ),
                errorDescription = if (status.is5xxServerError) {
                    null
                } else {
                    body["message"] as? String
                }
            ),
            status
        )
    }

    override fun getErrorPath(): String? {
        return null
    }

    private fun getErrorAttributeOptions(): ErrorAttributeOptions {
        var options = ErrorAttributeOptions.defaults()
        options = options.including(ErrorAttributeOptions.Include.EXCEPTION)
        options = options.including(ErrorAttributeOptions.Include.STACK_TRACE)
        options = options.including(ErrorAttributeOptions.Include.MESSAGE)
        options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS)

        return options
    }
}
