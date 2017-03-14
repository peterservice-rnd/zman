package com.peterservice.zman.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
open class SwaggerConfiguration {

    @Bean
    open fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.peterservice.zman.controllers"))
                .build()
                .apiInfo(buildApiInfo())
    }

    private fun buildApiInfo(): ApiInfo {
        val apiInfo = ApiInfo(
                "Zman REST API",
                null,
                "1.0.0",
                null,
                null as Contact?,
                null,
                null
        )
        return apiInfo
    }

}