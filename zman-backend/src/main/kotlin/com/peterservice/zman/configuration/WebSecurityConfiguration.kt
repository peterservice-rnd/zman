package com.peterservice.zman.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.firewall.DefaultHttpFirewall
import org.springframework.security.web.firewall.HttpFirewall
import javax.validation.constraints.NotNull


@ConditionalOnExpression("'\${authentication.type}' != 'NONE' && '\${authentication.type}' != 'KERBEROS'")
@Configuration
open class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/assets/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", false)
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // явное включение сессии
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

    @NotNull
    @Autowired
    lateinit var authenticationProvider: AuthenticationProvider
}