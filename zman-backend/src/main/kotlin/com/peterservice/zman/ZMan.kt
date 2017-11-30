package com.peterservice.zman

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class ZMan

fun main(args: Array<String>) {
    SpringApplication.run(ZMan::class.java, *args)
}
