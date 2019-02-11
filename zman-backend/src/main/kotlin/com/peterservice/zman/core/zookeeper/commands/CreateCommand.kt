package com.peterservice.zman.core.zookeeper.commands

import com.peterservice.zman.api.entities.ZNode
import com.peterservice.zman.core.zookeeper.audit.LoggedAction
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.extractParentPath
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.makeZPath
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.valueToBytes
import org.apache.curator.framework.CuratorFramework

class CreateCommand(private val client: CuratorFramework,
                    private val path: String,
                    private val node: ZNode,
                    private val overwrite: Boolean) {

    private val pathToCreate = makeZPath(path, node.key)

    private val nodesToCreate = ArrayList<NodeBaseInfo>()
    private val nodesToUpdate = ArrayList<NodeBaseInfo>()
    private val conflicts = ArrayList<String>()

    fun execute(actionBuilder: LoggedAction.Builder) : List<String> {
        collectNodes(pathToCreate, node)
        createNodes()

        if (conflicts.isEmpty()) {
            actionBuilder
                    .action("create")
                    .server(client.zookeeperClient.currentConnectionString)
                    .path(pathToCreate)
                    .newData(node.value)
        }

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

    private fun createNodes() {

        createParentNodesIfNeeded(pathToCreate)

        nodesToUpdate.forEach { (path, data) ->
            client.setData().forPath(path, data)
        }

        nodesToCreate.forEach { (path, data) ->
            client.create().forPath(path, data)
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
