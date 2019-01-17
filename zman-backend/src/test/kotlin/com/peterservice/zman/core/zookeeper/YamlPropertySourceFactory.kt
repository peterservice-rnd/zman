package com.peterservice.zman.core.zookeeper

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

open class YamlPropertySourceFactory : PropertySourceFactory {

    @Throws(IOException::class)
    override fun createPropertySource(name: String?, resource: EncodedResource): PropertiesPropertySource {
        val propertiesFromYaml = loadYamlIntoProperties(resource)
        val sourceName = name ?: resource.resource.filename
        return PropertiesPropertySource(sourceName, propertiesFromYaml)
    }

    @Throws(FileNotFoundException::class)
    private fun loadYamlIntoProperties(resource: EncodedResource): Properties {
        try {
            val factory = YamlPropertiesFactoryBean()
            factory.setResources(resource.resource)
            factory.afterPropertiesSet()
            return factory.`object`
        } catch (e: IllegalStateException) {
            // for ignoreResourceNotFound
            val cause = e.cause
            if (cause is FileNotFoundException)
                throw e.cause as FileNotFoundException
            throw e
        }
    }
}