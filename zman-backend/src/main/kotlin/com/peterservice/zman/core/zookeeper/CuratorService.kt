package com.peterservice.zman.core.zookeeper

import com.peterservice.zman.api.entities.ZNode
import com.peterservice.zman.api.zookeeper.ZookeeperService
import com.peterservice.zman.core.zookeeper.commands.CreateCommand
import com.peterservice.zman.core.zookeeper.commands.DeleteCommand
import com.peterservice.zman.core.zookeeper.commands.ReadCommand
import com.peterservice.zman.core.zookeeper.commands.UpdateCommand
import org.apache.curator.framework.CuratorFramework

class CuratorService(private val client: CuratorFramework) : ZookeeperService {

    override fun createZNode(path: String, znode: ZNode, overwrite: Boolean): List<String> {
        return CreateCommand(client, path, znode, overwrite).execute()
    }

    override fun readZNode(path: String, recursive: Boolean): ZNode {
        return ReadCommand(client, path, recursive).execute()
    }

    override fun updateZNode(path: String, znode: ZNode) {
        UpdateCommand(client, path, znode).execute()
    }

    override fun deleteZNode(path: String) {
        DeleteCommand(client, path).execute()
    }

    override fun close() {
        client.close()
    }
}
