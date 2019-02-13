package com.peterservice.zman.core.zookeeper.commands

import com.peterservice.zman.core.zookeeper.commands.CommandUtils.bytesToValue
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.makeZPath
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.throwNotFoundException
import com.peterservice.zman.core.zookeeper.events.ActionEvent
import mu.KLogging
import org.apache.curator.framework.CuratorFramework

class DeleteCommand(private val client: CuratorFramework,
                    private val path: String) {

    private companion object : KLogging()

    fun execute(actionEventBuilder: ActionEvent.Builder) {
        client.checkExists().forPath(path) ?: throwNotFoundException(path)
        actionEventBuilder
                .server(client.zookeeperClient.currentConnectionString)

        logChildren(actionEventBuilder, path)

        val oldBytes = client.data.forPath(path)
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path)
        actionEventBuilder.action("delete")
                .path(path)
                .oldData(bytesToValue(oldBytes))
                .add()
    }

    fun logChildren(actionEventBuilder: ActionEvent.Builder, path: String) {
        client.children.forPath(path).forEach {
            val zPath = makeZPath(path, it)
            val oldBytes = client.data.forPath(zPath)
            actionEventBuilder.action("delete")
                    .path(zPath)
                    .oldData(bytesToValue(oldBytes))
                    .add()
            logChildren(actionEventBuilder, zPath)
        }
    }
}
