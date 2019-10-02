package com.peterservice.zman.controllers

import com.peterservice.zman.api.services.AuthenticationFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {
    @Autowired
    private lateinit var authenticationFacade: AuthenticationFacade

    @GetMapping("/username")
    @ResponseStatus(HttpStatus.OK)
    fun getCurrentUserName(): String {
        return authenticationFacade.getAuthentication().name
    }
}