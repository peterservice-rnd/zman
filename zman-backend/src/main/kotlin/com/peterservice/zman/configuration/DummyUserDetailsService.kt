package com.peterservice.zman.configuration

import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

/**
 * Created by Alexey.Borodin on 30.01.2019.
 */
class DummyUserDetailsService: UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return User(username, "notUsed", true, true, true, true,
                AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN"))
    }
}