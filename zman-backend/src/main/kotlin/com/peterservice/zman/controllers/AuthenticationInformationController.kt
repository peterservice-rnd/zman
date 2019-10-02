package com.peterservice.zman.controllers

import com.peterservice.zman.api.entities.Authentication
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class AuthenticationInformationController {
    @GetMapping("/authentication")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun authentication(request: HttpServletRequest): Authentication {
        if (request.userPrincipal == null) {
            return Authentication("anonymousUser", false)
        }

        return Authentication(request.userPrincipal.name, true)
    }
}