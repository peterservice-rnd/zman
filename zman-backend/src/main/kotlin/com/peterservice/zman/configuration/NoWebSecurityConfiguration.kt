package com.peterservice.zman.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.firewall.DefaultHttpFirewall
import org.springframework.security.web.firewall.HttpFirewall

@ConditionalOnExpression("'\${authentication.type}' == 'NONE'")
@Configuration
open class NoWebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .anonymous()
            .and()
            .authorizeRequests()
            .antMatchers("/login*").denyAll()
            .antMatchers("/logout").denyAll()
            .and()
            .csrf()
            .disable()
    }

    @Bean
    open fun allowUrlEncodedSlashHttpFirewall(): HttpFirewall {
        val firewall = DefaultHttpFirewall()
        firewall.setAllowUrlEncodedSlash(true)
        return firewall
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall())
    }
}