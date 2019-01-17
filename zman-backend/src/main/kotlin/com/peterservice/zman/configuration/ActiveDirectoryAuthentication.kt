package com.peterservice.zman.configuration

import org.hibernate.validator.constraints.NotEmpty
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider
import org.springframework.validation.annotation.Validated

// Configuration for Active Directory authentication type

@Validated
@ConditionalOnExpression("'\${authentication.type}' == 'AD'")
@Configuration
@ConfigurationProperties("authentication.ad")
open class ActiveDirectoryAuthentication {

    @Bean
    open fun activeDirectoryLdapAuthenticationProvider(): AuthenticationProvider {
        return ActiveDirectoryLdapAuthenticationProvider(domain, url).apply {
            setConvertSubErrorCodesToExceptions(true)
            setUseAuthenticationRequestCredentials(true)
        }
    }

    @NotEmpty
    lateinit var domain: String
    @NotEmpty
    lateinit var url: String
}