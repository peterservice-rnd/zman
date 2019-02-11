package com.peterservice.zman.api.zookeeper

import com.peterservice.zman.api.entities.ZNode

interface ZookeeperService {

    fun createZNode(path: String, znode: ZNode, overwrite: Boolean, user: String?): List<String>

    fun readZNode(path: String, recursive: Boolean, user: String?): ZNode

    fun updateZNode(path: String, znode: ZNode, user: String?)

    fun deleteZNode(path: String, user: String?)

    fun close()
}