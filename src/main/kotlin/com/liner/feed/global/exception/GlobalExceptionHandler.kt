package com.liner.feed.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleExceptions(ex: Exception): ResponseEntity<Map<String, String?>> {
        val errors = mapOf(
            "errorMessage" to ErrorCodes.GLOBAL_ERROR.message,
            "statusCode" to HttpStatus.INTERNAL_SERVER_ERROR.name
        )

        return ResponseEntity(errors, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
        val errors = ex.bindingResult
            .allErrors
            .associateBy({ (it as FieldError).field }, { it.defaultMessage })

        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Map<String, String>> {
        val errorResponse = mapOf(
            "errorMessage" to ex.localizedMessage,
            "statusCode" to HttpStatus.BAD_REQUEST.name
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
}
