package com.peterservice.zman.core.zookeeper.commands

import com.peterservice.zman.api.entities.ZNode
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.bytesToValue
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.extractParentPath
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.makeZPath
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.valueToBytes
import com.peterservice.zman.core.zookeeper.events.ActionEvent
import org.apache.curator.framework.CuratorFramework

class CreateCommand(private val client: CuratorFramework,
                    private val path: String,
                    private val node: ZNode,
                    private val overwrite: Boolean) {

    private val pathToCreate = makeZPath(path, node.key)

    private val nodesToCreate = ArrayList<NodeBaseInfo>()
    private val nodesToUpdate = ArrayList<NodeBaseInfo>()
    private val conflicts = ArrayList<String>()

    fun execute(actionEventBuilder: ActionEvent.Builder) : List<String> {
        actionEventBuilder.server(client.zookeeperClient.currentConnectionString)

        collectNodes(pathToCreate, node)
        createNodes(actionEventBuilder)
        return conflicts
    }

    private fun collectNodes(pathToCreate: String, node: ZNode) {
        collectSingleZnode(pathToCreate, node)
        node.children.forEach { childNode ->
            val childPath = makeZPath(pathToCreate, childNode.key)
            collectNodes(childPath, childNode)
        }
    }

    fun collectSingleZnode(pathToCreate: String, node: ZNode) {
        val bytes = valueToBytes(node.value)
        val stat = client.checkExists().forPath(pathToCreate)
        if (stat != null) {
            if (overwrite) {
                nodesToUpdate += NodeBaseInfo(pathToCreate, bytes)
                return
            }
            conflicts += pathToCreate
            return
        }
        nodesToCreate += NodeBaseInfo(pathToCreate, bytes)
    }

    private fun createNodes(builder: ActionEvent.Builder) {

        createParentNodesIfNeeded(pathToCreate)

        nodesToUpdate.forEach { (path, data) ->
            val oldBytes = client.data.forPath(path)
            client.setData().forPath(path, data)
            builder
                    .action("update")
                    .path(path)
                    .oldData(bytesToValue(oldBytes))
                    .newData(bytesToValue(data))
                    .add()
        }

        nodesToCreate.forEach { (path, data) ->
            client.create().forPath(path, data)
            builder
                    .action("create")
                    .path(path)
                    .newData(bytesToValue(data))
                    .add()
        }

    }

    private fun createParentNodesIfNeeded(pathToCreate: String) {
        val parentPath = extractParentPath(pathToCreate)
        if (parentPath == "/") return
        val stat = client.checkExists().forPath(parentPath)
        if (stat == null) {
            client.create().creatingParentsIfNeeded().forPath(parentPath)
        }
    }

}
