package com.peterservice.zman.configuration

import org.hibernate.validator.constraints.NotEmpty
import org.springframework.beans.factory.BeanInitializationException
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.ldap.pool.factory.PoolingContextSource
import org.springframework.ldap.pool.validation.DefaultDirContextValidator
import org.springframework.ldap.pool.validation.DirContextValidator
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.ldap.authentication.BindAuthenticator
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch
import org.springframework.util.StringUtils
import org.springframework.validation.annotation.Validated

@Validated
@ConditionalOnExpression("'\${authentication.type}' == 'LDAP'")
@Configuration
@ConfigurationProperties("authentication.ldap")
open class GenericLDAPAuthentication {

    @Bean
    open fun ldapContextSourceTarget(): LdapContextSource {
        return LdapContextSource().apply {
            setUrl(url)
            setCacheEnvironmentProperties(false) // default caching set to 'true' causes NPE in Spring
            setBase(base)

            // we need some user name/password to make the directory search, as anonymous search will highly unlikely be possible
            if (!StringUtils.isEmpty(userDN) && !StringUtils.isEmpty(userPassword)) {
                userDn = userDN
                password = userPassword
            }
        }
    }

    @Bean
    open fun dirContextValidator(): DirContextValidator = DefaultDirContextValidator()

    @Bean
    open fun contextSource(targetContext: LdapContextSource, contextValidator: DirContextValidator): PoolingContextSource {
        return PoolingContextSource().apply {
            contextSource = targetContext
            dirContextValidator = contextValidator
            testOnBorrow = true
            testWhileIdle = true
        }
    }

    @Bean
    open fun ldapAuthenticationProvider(contextSource: LdapContextSource): AuthenticationProvider {
        val bindAuthenticator = BindAuthenticator(contextSource)

        // userDnPatterns may be empty, then userSearchFilter will be used to find user DN
        if (!userDnPatterns.isEmpty()) {
            bindAuthenticator.setUserDnPatterns(userDnPatterns.toTypedArray())
        }

        if (userSearchFilter.isEmpty() || userSearchBase.isEmpty()) {
            if (userDnPatterns.isEmpty()) {
                throw BeanInitializationException("ldap.userSearchFilter and ldap.userSearchBase should be provided in when userDnPatterns is empty")
            }
        } else {
            val userSearch = FilterBasedLdapUserSearch(userSearchBase, userSearchFilter, contextSource)
            userSearch.setSearchSubtree(true)
            bindAuthenticator.setUserSearch(userSearch)
        }

        return LdapAuthenticationProvider(bindAuthenticator)
    }

    var userDnPatterns: List<String> = ArrayList()
    var userSearchFilter: String = ""
    var userSearchBase: String = ""
    var userDN: String = ""
    var userPassword: String = ""
    @NotEmpty
    var url: String = ""
    @NotEmpty
    var base: String = ""
}