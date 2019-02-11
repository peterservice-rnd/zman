package com.peterservice.zman.configuration

import org.hibernate.validator.constraints.NotEmpty
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.kerberos.authentication.KerberosAuthenticationProvider
import org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosClient
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter
import org.springframework.security.kerberos.web.authentication.SpnegoEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.firewall.DefaultHttpFirewall
import org.springframework.security.web.firewall.HttpFirewall




@ConditionalOnExpression("'\${authentication.type}' == 'KERBEROS'")
@ConfigurationProperties("authentication.kerberos")
@Configuration
@EnableWebSecurity
open class WebSecurityKerberosConfiguration : WebSecurityConfigurerAdapter() {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
                .authenticationProvider(kerberosAuthenticationProvider())
                .authenticationProvider(kerberosServiceAuthenticationProvider())

    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .exceptionHandling()
                    .authenticationEntryPoint(spnegoEntryPoint())
                .and()
                .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/assets/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/", false)
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .permitAll()
                    .deleteCookies("JSESSIONID")
                .and()
                .addFilterBefore(
                        spnegoAuthenticationProcessingFilter(),
                        BasicAuthenticationFilter::class.java)
    }

    //@Bean
    open fun kerberosAuthenticationProvider(): KerberosAuthenticationProvider {
        val provider = KerberosAuthenticationProvider()
        val client = SunJaasKerberosClient()
        client.setDebug(true)
        provider.setKerberosClient(client)
        provider.setUserDetailsService(dummyUserDetailsService())
        return provider
    }

    @Bean
    open fun spnegoEntryPoint(): SpnegoEntryPoint {
        return SpnegoEntryPoint("/login")
    }

    @Bean
    open fun spnegoAuthenticationProcessingFilter(): SpnegoAuthenticationProcessingFilter {
        val filter = SpnegoAuthenticationProcessingFilter()
        filter.setAuthenticationManager(authenticationManagerBean())
        return filter
    }

    //@Bean
    open fun kerberosServiceAuthenticationProvider(): KerberosServiceAuthenticationProvider {
        val provider = KerberosServiceAuthenticationProvider()
        provider.setTicketValidator(sunJaasKerberosTicketValidator())
        provider.setUserDetailsService(dummyUserDetailsService())
        return provider
    }

    @Bean
    open fun sunJaasKerberosTicketValidator(): SunJaasKerberosTicketValidator {
        val ticketValidator = SunJaasKerberosTicketValidator()
        ticketValidator.setServicePrincipal(servicePrincipal) //At this point, it must be according to what we were given in the commands from the first step.
        val fs = FileSystemResource(keytabFilePath) //Path to file tomcat.keytab
        logger.info("Initializing Kerberos KEYTAB file path:" + fs.getFilename() + " for principal: " + servicePrincipal + "file exist: " + fs.exists())
        ticketValidator.setKeyTabLocation(fs)
        ticketValidator.setDebug(true) //Turn off when it will works properly,
        return ticketValidator
    }

    @Bean
    open fun dummyUserDetailsService(): DummyUserDetailsService {
        return DummyUserDetailsService()
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

    @NotEmpty
    lateinit var servicePrincipal: String

    @NotEmpty
    lateinit var keytabFilePath: String
}