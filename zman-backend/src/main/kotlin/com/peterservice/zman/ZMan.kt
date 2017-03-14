package com.peterservice.zman

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
open class ZMan

fun main(args: Array<String>) {
    SpringApplication.run(ZMan::class.java, *args)
}
