package com.peterservice.zman.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter


@Configuration
open class WebMvcConfiguration : WebMvcConfigurerAdapter() {

    @Value("\${frontend.path:frontend}")
    private lateinit var frontendPath: String

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        with(registry) {
            addResourceHandler("/assets/**").addResourceLocations("file:$frontendPath/assets/")
            addResourceHandler("/index.html").addResourceLocations("file:$frontendPath/index.html")
            addResourceHandler("/login.html").addResourceLocations("file:$frontendPath/login.html")
        }
    }

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/login").setViewName("forward:/login.html")
        registry.addViewController("/").setViewName("forward:/index.html")
    }

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        with(configurer) {
            favorPathExtension(false)
            favorParameter(true)
            ignoreAcceptHeader(false)
            useJaf(false)
            defaultContentType(MediaType.APPLICATION_JSON)
            mediaType("xml", MediaType.APPLICATION_XML)
            mediaType("json", MediaType.APPLICATION_JSON)
        }
    }

    @Bean
    open fun zpathFilterRegistration(): FilterRegistrationBean {
        val registration = FilterRegistrationBean()
        registration.filter = ZpathFilter()
        registration.addUrlPatterns("/api/zk/*")
        return registration
    }

}
