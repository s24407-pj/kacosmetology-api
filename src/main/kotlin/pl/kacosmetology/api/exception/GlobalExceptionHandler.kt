package pl.kacosmetology.api.exception

import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler
    fun handleResourceNotFoundException(ex: ResourceNotFoundException): ResponseEntity<ApiError> {
        val apiError = ApiError(NOT_FOUND.value(), ex.message)
        return ResponseEntity(apiError, NOT_FOUND)
    }

    @ExceptionHandler
    fun handleResourceConflictException(ex: ResourceConflictException): ResponseEntity<ApiError> {
        val apiError = ApiError(CONFLICT.value(), ex.message)
        return ResponseEntity(apiError, CONFLICT)
    }

    @ExceptionHandler
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = ex.bindingResult.fieldErrors.map {
            mapOf("field" to it.field, "error" to (it.defaultMessage ?: "Błąd walidacji"))
        }
        return ResponseEntity(mapOf("errors" to errors), BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleInvalidTokenException(ex: InvalidTokenException): ResponseEntity<ApiError> {
        val apiError = ApiError(UNAUTHORIZED.value(), ex.message)
        return ResponseEntity(apiError, UNAUTHORIZED)
    }

//    @ExceptionHandler(Exception::class)
//    fun handleGlobalException(ex: Exception): ResponseEntity<ApiError> {
//        val apiError = ApiError(INTERNAL_SERVER_ERROR.value(), "Wystąpił nieoczekiwany błąd")
//        return ResponseEntity(apiError, INTERNAL_SERVER_ERROR)
//    }
}


