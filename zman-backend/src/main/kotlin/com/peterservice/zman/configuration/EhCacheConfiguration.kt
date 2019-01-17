package com.peterservice.zman.configuration

import net.sf.ehcache.Cache
import net.sf.ehcache.CacheManager
import net.sf.ehcache.config.CacheConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class EhCacheConfiguration {

    @Value("\${zman.cache.max-elements:20}")
    private var maxElements: Int = 20
    @Value("\${zman.cache.time-to-idle-seconds:1800}")
    private var timeToIdleSeconds: Long = 1800

    @Bean
    open fun cache(): Cache {
        val managerConfiguration = net.sf.ehcache.config.Configuration()
        managerConfiguration
                .name("config")
                .cache(CacheConfiguration()
                        .name("ZookeeperServiceCache")
                        .eternal(false)
                        .maxEntriesLocalHeap(maxElements)
                        .timeToIdleSeconds(timeToIdleSeconds)
                )
        val manager = CacheManager.create(managerConfiguration)
        val cache = manager.getCache("ZookeeperServiceCache")
        cache.cacheEventNotificationService.registerListener(ZookeeperServiceCacheEventListener())
        return cache
    }
}
