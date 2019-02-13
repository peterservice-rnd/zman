package com.peterservice.zman.core.zookeeper

import com.peterservice.zman.api.entities.ZNode
import com.peterservice.zman.api.zookeeper.ZookeeperService
import com.peterservice.zman.core.zookeeper.commands.CreateCommand
import com.peterservice.zman.core.zookeeper.commands.DeleteCommand
import com.peterservice.zman.core.zookeeper.commands.ReadCommand
import com.peterservice.zman.core.zookeeper.commands.UpdateCommand
import com.peterservice.zman.core.zookeeper.events.ActionEvent
import com.peterservice.zman.core.zookeeper.events.ActionListener
import org.apache.curator.framework.CuratorFramework

class CuratorService(private val client: CuratorFramework, vararg val listenerServices: ActionListener) : ZookeeperService {

    override fun createZNode(path: String, znode: ZNode, overwrite: Boolean, user: String?): List<String> {
        val builder: ActionEvent.Builder = ActionEvent.Builder().user(user)
        val conflicts = CreateCommand(client, path, znode, overwrite).execute(builder)
        val action = builder.build()
        if (!action.isEmpty()) {
            listenerServices.forEach { it -> it.handle(action) }
        }
        return conflicts
    }

    override fun readZNode(path: String, recursive: Boolean, userUnused: String?): ZNode {
        val zNode = ReadCommand(client, path, recursive).execute()
        return zNode
    }

    override fun updateZNode(path: String, znode: ZNode, user: String?) {
        val builder: ActionEvent.Builder = ActionEvent.Builder().user(user)
        UpdateCommand(client, path, znode).execute(builder)
        val action = builder.build()
        if (!action.isEmpty()) {
            listenerServices.forEach { it -> it.handle(action) }
        }
    }

    override fun deleteZNode(path: String, user: String?) {
        val builder: ActionEvent.Builder = ActionEvent.Builder().user(user)
        DeleteCommand(client, path).execute(builder)
        val action = builder.build()
        if (!action.isEmpty()) {
            listenerServices.forEach { it -> it.handle(action) }
        }
    }

    override fun close() {
        client.close()
    }
}
