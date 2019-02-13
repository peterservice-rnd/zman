package com.peterservice.zman.core.zookeeper.commands

import com.peterservice.zman.api.entities.ZNode
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.throwNotFoundException
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.valueToBytes
import com.peterservice.zman.core.zookeeper.events.ActionEvent
import org.apache.curator.framework.CuratorFramework

class UpdateCommand(private val client: CuratorFramework,
                    private val path: String,
                    private val node: ZNode) {

    fun execute(actionEventBuilder: ActionEvent.Builder) {
        client.checkExists().forPath(path) ?: throwNotFoundException(path)
        val oldBytes = client.data.forPath(path)
        val bytes = valueToBytes(node.value)
        client.setData().forPath(path, bytes)

        actionEventBuilder
                .server(client.zookeeperClient.currentConnectionString)
                .action("update")
                .path(path)
                .oldData(CommandUtils.bytesToValue(oldBytes))
                .newData(node.value)
                .add()
    }

}
