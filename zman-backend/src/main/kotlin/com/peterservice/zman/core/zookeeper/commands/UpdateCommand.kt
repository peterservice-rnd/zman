package com.peterservice.zman.core.zookeeper.commands

import com.peterservice.zman.api.entities.ZNode
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.throwNotFoundException
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.valueToBytes
import org.apache.curator.framework.CuratorFramework

class UpdateCommand(private val client: CuratorFramework,
                    private val path: String,
                    private val node: ZNode) {

    fun execute() {
        client.checkExists().forPath(path) ?: throwNotFoundException(path)
        val bytes = valueToBytes(node.value)
        client.setData().forPath(path, bytes)
    }

}
