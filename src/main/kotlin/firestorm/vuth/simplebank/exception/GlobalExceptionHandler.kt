package firestorm.vuth.simplebank.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BusinessRuleException::class)
    fun handleBusinessRule(ex: BusinessRuleException): ResponseEntity<Any> {
        return ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException): ResponseEntity<Any> {
        return ResponseEntity(mapOf("error" to ex.message), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handleOtherException(ex: Exception): ResponseEntity<Any> {
        return ResponseEntity(mapOf("error" to ex.message), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}