package org.zerock.apiserver.controller.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.zerock.apiserver.controller.CoffeeOrderController
import org.zerock.apiserver.service.CoffeeOrderServiceImpl
import org.zerock.apiserver.util.CustomJWTException

@RestControllerAdvice(basePackageClasses = [CoffeeOrderController::class])
class CustomControllerAdvice {

    @ExceptionHandler(NoSuchElementException::class)
    fun noExit(e: NoSuchElementException): ResponseEntity<Any> {
        val msg = e.message ?: "error"
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("msg" to msg))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleIllegalArgumentException(e: MethodArgumentNotValidException): ResponseEntity<Any> {

        val msg = e.message

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(mapOf("msg" to msg))
    }

    @ExceptionHandler(CustomJWTException::class)
    fun handleJWTException(e : CustomJWTException):ResponseEntity<Any> {
        val msg = e.message

        return ResponseEntity.ok().body(mapOf("error" to msg))
    }
}