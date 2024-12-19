package pl.kacosmetology.api.exception

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    fun handleResourceNotFoundException(ex: ResourceNotFoundException): ResponseEntity<ApiError> {
        val apiError = ApiError(NOT_FOUND.value(), ex.message)
        return ResponseEntity(apiError, NOT_FOUND)
    }
}


