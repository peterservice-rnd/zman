package com.peterservice.zman.api.zookeeper

import com.peterservice.zman.api.entities.ZServer

interface ZookeeperServiceManager {

    fun getServiceFor(server: ZServer): ZookeeperService

    fun invalidateCacheFor(server: ZServer)
}
