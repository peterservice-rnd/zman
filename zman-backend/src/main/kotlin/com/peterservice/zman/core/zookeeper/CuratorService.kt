package com.peterservice.zman.core.zookeeper

import com.peterservice.zman.api.entities.ZNode
import com.peterservice.zman.api.zookeeper.ZookeeperService
import com.peterservice.zman.core.zookeeper.audit.ActionLogger
import com.peterservice.zman.core.zookeeper.audit.LoggedAction
import com.peterservice.zman.core.zookeeper.commands.CreateCommand
import com.peterservice.zman.core.zookeeper.commands.DeleteCommand
import com.peterservice.zman.core.zookeeper.commands.ReadCommand
import com.peterservice.zman.core.zookeeper.commands.UpdateCommand
import org.apache.curator.framework.CuratorFramework

class CuratorService(private val client: CuratorFramework, private val loggerService: ActionLogger) : ZookeeperService {

    override fun createZNode(path: String, znode: ZNode, overwrite: Boolean, user: String?): List<String> {
        val builder: LoggedAction.Builder = LoggedAction.Builder().user(user)
        val conflicts = CreateCommand(client, path, znode, overwrite).execute(builder)
        val action = builder.build()
        if (!action.isEmpty()) {
            loggerService.log(action)
        }
        return conflicts
    }

    override fun readZNode(path: String, recursive: Boolean, user: String?): ZNode {
        val builder: LoggedAction.Builder = LoggedAction.Builder().user(user)
        val zNode = ReadCommand(client, path, recursive).execute(builder)
        loggerService.log(builder.build())
        return zNode
    }

    override fun updateZNode(path: String, znode: ZNode, user: String?) {
        val builder: LoggedAction.Builder = LoggedAction.Builder().user(user)
        UpdateCommand(client, path, znode).execute(builder)
        loggerService.log(builder.build())
    }

    override fun deleteZNode(path: String, user: String?) {
        val builder: LoggedAction.Builder = LoggedAction.Builder().user(user)
        DeleteCommand(client, path).execute(builder)
        loggerService.log(builder.build())
    }

    override fun close() {
        client.close()
    }
}
