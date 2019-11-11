package com.peterservice.zman.controllers

import com.peterservice.zman.api.data.Authentication
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class AuthenticationInformationController {
    @GetMapping("/authentication")
    @ResponseStatus(HttpStatus.OK)
    fun authentication(request: HttpServletRequest): Authentication {
        if (request.userPrincipal == null) {
            return Authentication(false)
        }

        return Authentication((request.userPrincipal as UsernamePasswordAuthenticationToken).isAuthenticated, request.userPrincipal.name)
    }
}