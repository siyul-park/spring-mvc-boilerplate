package io.github.siyual_park.handler

import io.github.siyual_park.model.ErrorResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class DatabaseExceptionHandler {

    @ExceptionHandler(EmptyResultDataAccessException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handle(exception: EmptyResultDataAccessException): ErrorResponse {
        return ErrorResponse(exception.message)
    }

    @ExceptionHandler(EntityExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handle(exception: EntityExistsException): ErrorResponse {
        return ErrorResponse(exception.message)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handle(exception: EntityNotFoundException): ErrorResponse {
        return ErrorResponse(exception.message)
    }
}
