package com.peterservice.zman.core.zookeeper.commands

import com.peterservice.zman.api.entities.ZNode
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.extractParentPath
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.makeZPath
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.valueToBytes
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.api.transaction.CuratorOp

class CreateCommand(private val client: CuratorFramework,
                    private val path: String,
                    private val node: ZNode,
                    private val overwrite: Boolean) {

    private val nodesToCreate = ArrayList<NodeBaseInfo>()
    private val nodesToUpdate = ArrayList<NodeBaseInfo>()
    private val conflicts = ArrayList<String>()

    fun execute(): List<String> {
        val pathToCreate = makeZPath(path, node.key)
        createParentNodesIfNeeded(pathToCreate)
        collectNodesToCreate(pathToCreate, node)
        createInTransaction()
        return conflicts
    }

    private fun createParentNodesIfNeeded(pathToCreate: String) {
        val parentPath = extractParentPath(pathToCreate)
        if (parentPath == "/") return
        val stat = client.checkExists().forPath(parentPath)
        if (stat == null) {
            client.create().creatingParentsIfNeeded().forPath(parentPath)
        }
    }

    private fun collectNodesToCreate(pathToCreate: String, node: ZNode) {
        collectSingleZnode(pathToCreate, node)
        node.children.forEach { childNode ->
            val childPath = makeZPath(pathToCreate, childNode.key)
            collectNodesToCreate(childPath, childNode)
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

    private fun createInTransaction() {
        val operations = mutableListOf<CuratorOp>()

        for ((path, data) in nodesToUpdate) {
            operations += client.transactionOp().setData().forPath(path, data)
        }
        for ((path, data) in nodesToCreate) {
            operations += client.transactionOp().create().forPath(path, data)
        }

        if (operations.isEmpty()) return

        client.transaction().forOperations(operations)
    }

}
