package com.peterservice.zman.api.services

import org.springframework.security.core.Authentication

interface AuthenticationFacade {
    fun getAuthentication(): Authentication
}