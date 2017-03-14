package com.peterservice.zman.core.zookeeper

import com.peterservice.zman.api.zookeeper.ZookeeperService
import com.peterservice.zman.api.zookeeper.ZookeeperServicePool
import net.sf.ehcache.Cache
import net.sf.ehcache.Element
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@EnableScheduling
class DefaultZookeeperServicePool : ZookeeperServicePool {

    @Autowired
    private lateinit var cache: Cache

    override fun getService(id: Long): ZookeeperService {
        return cache.get(id).objectValue as ZookeeperService
    }

    override fun putService(id: Long, service: ZookeeperService) {
        cache.put(Element(id, service))
    }

    override operator fun contains(id: Long): Boolean {
        return cache.isKeyInCache(id)
    }

    override fun removeService(id: Long) {
        cache.remove(id)
    }

    @Scheduled(fixedDelayString = "#{\${zman.cache.eviction-delay-seconds:60}*1000}")
    override fun closeExpiredServices() {
        cache.evictExpiredElements()
    }
}
