package com.peterservice.zman.controllers

import com.peterservice.zman.api.entities.Message
import com.peterservice.zman.api.exceptions.AliasAlreadyExistsException
import com.peterservice.zman.api.exceptions.AliasNotFoundException
import com.peterservice.zman.api.exceptions.ZNodeNotFoundException
import com.peterservice.zman.api.exceptions.ZookeeperConnectionException
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.validation.ConstraintViolationException

@ControllerAdvice
open class ExceptionHandlingController {

    private companion object : KLogging()

    @ExceptionHandler(value = ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun constraintViolationExceptionHandler(e: ConstraintViolationException): Message {
        val body = e.constraintViolations
                .map { it.message }
                .joinToString(separator = ", \n ")
        return Message(e.javaClass.simpleName, body)
    }

    @ExceptionHandler(value = AliasNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun aliasNotFoundHandler(e: AliasNotFoundException): Message {
        return standardMessage(e)
    }

    @ExceptionHandler(value = AliasAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun aliasAlreadyExistsHandler(e: AliasAlreadyExistsException): Message {
        return standardMessage(e)
    }

    @ExceptionHandler(value = ZNodeNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun znodeNotFoundHandler(e: ZNodeNotFoundException): Message {
        return standardMessage(e)
    }

    @ExceptionHandler(value = ZookeeperConnectionException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun zookeeperConnectionExceptionHandler(e: ZookeeperConnectionException): Message {
        logger.error(e.message, e)
        return standardMessage(e)
    }

    @ExceptionHandler(value = Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun defaultExceptionHandler(e: Exception): Message {
        logger.error(e.message, e)
        return standardMessage(e)
    }

    private fun standardMessage(e: Exception): Message {
        return Message(e.javaClass.simpleName, e.message.orEmpty())
    }

}
