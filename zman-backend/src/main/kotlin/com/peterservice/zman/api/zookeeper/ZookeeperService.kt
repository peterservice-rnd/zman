package com.peterservice.zman.api.zookeeper

import com.peterservice.zman.api.entities.ZNode

interface ZookeeperService {

    fun createZNode(path: String, znode: ZNode, overwrite: Boolean): List<String>

    fun readZNode(path: String, recursive: Boolean): ZNode

    fun updateZNode(path: String, znode: ZNode)

    fun deleteZNode(path: String)

    fun close()
}