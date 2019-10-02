package com.peterservice.zman.api.services

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component


@Component
class AuthenticationFacadeDefault : AuthenticationFacade {
    override fun getAuthentication(): Authentication {
        return SecurityContextHolder.getContext().authentication
    }
}