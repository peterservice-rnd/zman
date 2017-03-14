package com.peterservice.zman.api.zookeeper

interface ZookeeperServicePool {

    fun getService(id: Long): ZookeeperService

    fun putService(id: Long, service: ZookeeperService)

    operator fun contains(id: Long): Boolean

    fun removeService(id: Long)

    fun closeExpiredServices()

}