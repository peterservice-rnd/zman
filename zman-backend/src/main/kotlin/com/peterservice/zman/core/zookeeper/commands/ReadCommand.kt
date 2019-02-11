package com.peterservice.zman.core.zookeeper.commands

import com.peterservice.zman.api.entities.ZNode
import com.peterservice.zman.core.zookeeper.audit.LoggedAction
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.bytesToValue
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.makeZPath
import com.peterservice.zman.core.zookeeper.commands.CommandUtils.throwNotFoundException
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.utils.ZKPaths.getNodeFromPath
import java.util.*

class ReadCommand(private val client: CuratorFramework,
                  private val path: String,
                  private val recursive: Boolean) {

    fun execute(actionBuilder: LoggedAction.Builder): ZNode {
        val stat = client.checkExists().forPath(path) ?: throwNotFoundException(path)
        val bytes = client.data.forPath(path)
        val parentNodeData = NodeBaseInfo(path, bytes, stat)

        actionBuilder
                .action("read")
                .server(client.zookeeperClient.currentConnectionString)
                .path(path)

        return buildZNode(parentNodeData)
    }

    private fun buildZNode(parent: NodeBaseInfo): ZNode {
        val children = client.children.forPath(parent.path)
                .map { getChildData(makeZPath(parent.path, it)) }
        val nodes = children
                .map { if (recursive) buildZNode(it) else buildSingleZNode(it) }
                .sortedWith(comparator)
        return buildSingleZNode(parent, nodes)
    }

    private fun buildSingleZNode(nodeData: NodeBaseInfo, children: List<ZNode> = emptyList()): ZNode {
        return ZNode(
                path = nodeData.path,
                key = getNodeFromPath(nodeData.path),
                value = bytesToValue(nodeData.data),
                hasChildren = nodeData.stat?.numChildren ?: children.size > 0,
                children = children
        )
    }

    private fun getChildData(path: String): NodeBaseInfo {
        val stat = client.checkExists().forPath(path)
        val data = client.data.forPath(path)
        return NodeBaseInfo(path, data, stat)
    }

    companion object {
        private val hasChildrenComparator = fun(one: ZNode, another: ZNode): Int {
            if (!one.hasChildren && another.hasChildren) return 1
            if (one.hasChildren && !another.hasChildren) return -1
            return 0
        }

        private val keysComparator = fun(one: ZNode, another: ZNode): Int {
            return one.key.compareTo(another.key)
        }

        private val comparator = Comparator(hasChildrenComparator).thenComparing(keysComparator)
    }

}
