package com.peterservice.zman.core.zookeeper

import com.peterservice.zman.api.entities.ZServer
import com.peterservice.zman.api.zookeeper.ZookeeperService
import com.peterservice.zman.api.zookeeper.ZookeeperServiceManager
import com.peterservice.zman.api.zookeeper.ZookeeperServicePool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CuratorServiceManager : ZookeeperServiceManager {

    @Autowired
    private lateinit var zookeeperServicePool: ZookeeperServicePool

    @Autowired
    private lateinit var curatorServiceFactory: CuratorServiceFactory

    @Synchronized
    override fun getServiceFor(server: ZServer): ZookeeperService {
        zookeeperServicePool.closeExpiredServices()
        if (!zookeeperServicePool.contains(server.id)) {
            val service = curatorServiceFactory.create(server)
            zookeeperServicePool.putService(server.id, service)
            return service
        }
        return zookeeperServicePool.getService(server.id)
    }

    override fun invalidateCacheFor(server: ZServer) {
        if (zookeeperServicePool.contains(server.id)) {
            zookeeperServicePool.removeService(server.id)
        }
    }
}
