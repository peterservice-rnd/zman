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
        }
    }

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/").setViewName("forward:/index.html")
    }

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        with(configurer) {
            favorPathExtension(false)
            ignoreAcceptHeader(true)
            favorParameter(true)
            defaultContentType(MediaType.APPLICATION_JSON)
            mediaType("json", MediaType.APPLICATION_JSON)
            mediaType("xml", MediaType.APPLICATION_XML)
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
