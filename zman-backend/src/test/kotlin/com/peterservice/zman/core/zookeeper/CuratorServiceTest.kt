package com.peterservice.zman.core.zookeeper

import com.google.common.base.Charsets.UTF_8
import com.peterservice.zman.api.entities.ZNode
import com.peterservice.zman.api.entities.ZServer
import com.peterservice.zman.api.exceptions.ZNodeNotFoundException
import com.peterservice.zman.core.zookeeper.audit.ActionLogger
import com.peterservice.zman.core.zookeeper.audit.LoggedAction
import junit.framework.TestCase.*
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.RetryNTimes
import org.apache.curator.test.TestingServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class CuratorServiceTest {

    private lateinit var curatorService: CuratorService
    private lateinit var connectionString: String
    private lateinit var client: CuratorFramework
    private lateinit var logger: ActionLogger

    @Before
    fun setUp() {
        logger = mock(ActionLogger::class.java)
        val testingServer = TestingServer(true)
        val curatorServiceFactory = CuratorServiceFactory(logger)
        connectionString = testingServer.connectString

        curatorService = curatorServiceFactory.create(ZServer().apply {
            this.connectionString = testingServer.connectString
        })

        client = CuratorFrameworkFactory.newClient(
                testingServer.connectString,
                RetryNTimes(1, 0)
        )
        client.start()

        client.create().forPath("/path", "pathValue".toByteArray(UTF_8))
        client.create().forPath("/path/a", "aValue".toByteArray(UTF_8))
        client.create().forPath("/path/b", "bValue".toByteArray(UTF_8))
        client.create().forPath("/path/b/c", "cValue".toByteArray(UTF_8))
        client.create().forPath("/path/b/d", "dValue".toByteArray(UTF_8))
        client.create().forPath("/path/b/e", "eValue".toByteArray(UTF_8))
        client.create().forPath("/path/b/e/f", "fValue".toByteArray(UTF_8))
    }

    @After
    fun tearDown() {
        client.close()
    }

    private val user: String = "user"

    @Test
    fun testReadZNodeNotFound() {
        try {
            curatorService.readZNode("/path/for/nowhere", false, user)
            fail("no exception caught")
        } catch (e: Exception) {
            assertTrue(e is ZNodeNotFoundException)
            assertEquals("ZNode '/path/for/nowhere' was not found", e.message)
        }

    }

    @Test
    fun testReadZNodeNotFoundNotLogged() {
        try {
            curatorService.readZNode("/path/for/nowhere", false, user)
        } catch (e: Exception) {
        }

        verifyZeroInteractions(logger)
    }

    @Test
    fun testReadZNode() {
        val nodeExpected = ZNode("path", "pathValue", true, listOf(
                ZNode("b", "bValue", true, path = "/path/b"),
                ZNode("a", "aValue", false, path = "/path/a")
        ), path = "/path")

        val nodeActual = curatorService.readZNode("/path", false, user)

        assertEquals(nodeExpected, nodeActual)
    }

    @Test
    fun testReadZNodeLogged() {
        curatorService.readZNode("/path", false, user)
        verify(logger).log(LoggedAction.Builder().user(user).server(connectionString).action("read").path("/path").build())
    }

    @Test
    fun testReadZNodeRecursive() {
        val nodeExpected = ZNode("path", "pathValue", true, listOf(
                ZNode("b", "bValue", true, listOf(
                        ZNode("e", "eValue", true, listOf(
                                ZNode("f", "fValue", false, path = "/path/b/e/f")
                        ), path = "/path/b/e"),
                        ZNode("c", "cValue", false, path = "/path/b/c"),
                        ZNode("d", "dValue", false, path = "/path/b/d")
                ), path = "/path/b"),
                ZNode("a", "aValue", false, path = "/path/a")
        ), path = "/path")

        val nodeActual = curatorService.readZNode("/path", true, user)

        assertEquals(nodeExpected, nodeActual)

    }

    @Test
    fun testDeleteZNodeNotFound() {
        try {
            curatorService.deleteZNode("/path/for/nowhere", user)
            fail("no exception caught")
        } catch (e: Exception) {
            assertTrue(e is ZNodeNotFoundException)
            assertEquals("ZNode '/path/for/nowhere' was not found", e.message)
        }
    }

    @Test
    fun testDeleteZNodeNotFoundNotLogged() {
        try {
            curatorService.deleteZNode("/path/for/nowhere", user)
        } catch (e: Exception) {
        }
        verifyZeroInteractions(logger)
    }

    @Test
    fun testDeleteZNode() {
        curatorService.deleteZNode("/path", user)
        assertEquals(null, client.checkExists().forPath("/path"))
    }

    @Test
    fun testDeleteZNodeLogged() {
        curatorService.deleteZNode("/path", user)
        verify(logger).log(LoggedAction.Builder().action("delete").server(connectionString).user(user).path("/path").build())
    }

    @Test
    fun testUpdateZNodeNotFound() {
        try {
            curatorService.updateZNode("/path/for/nowhere", ZNode("", null, false), user)
            fail("no exception caught")
        } catch (e: Exception) {
            assertTrue(e is ZNodeNotFoundException)
            assertEquals("ZNode '/path/for/nowhere' was not found", e.message)
        }
    }

    @Test
    fun testUpdateZNodeNotFoundNotLogged() {
        try {
            curatorService.updateZNode("/path/for/nowhere", ZNode("", null, false), user)
        } catch (e: Exception) {
        }
        verifyZeroInteractions(logger)
    }

    @Test
    fun testUpdateZNode() {
        curatorService.updateZNode("/path/b", ZNode("b", "newBValue", false), user)
        assertEquals("newBValue", String(client.data.forPath("/path/b"), UTF_8))
    }

    @Test
    fun testUpdateZNodeLogged() {
        curatorService.updateZNode("/path/b", ZNode("b", "newBValue", false), user)
        verify(logger).log(LoggedAction.Builder().action("update").server(connectionString).user(user).path("/path/b").oldData("bValue").newData("newBValue").build())
    }

    @Test
    fun testCreateZNode() {
        val conflicts = curatorService.createZNode("/path/b", ZNode("newNode", "newNodeValue", false), false, user)

        assertEquals("newNodeValue", String(client.data.forPath("/path/b/newNode"), UTF_8))
        assertTrue(conflicts.isEmpty())
    }

    @Test
    fun testCreateZNodeLogged() {
        curatorService.createZNode("/path/b", ZNode("newNode", "newNodeValue", false), false, user)
        verify(logger).log(LoggedAction.Builder().action("create").server(connectionString).user(user).path("/path/b/newNode").newData("newNodeValue").build())
    }

    @Test
    fun testCreateZNodeCreatingParents() {
        val conflicts = curatorService.createZNode("/path/for2/some/parent/path", ZNode("newNode", "newNodeValue", false), false, user)

        assertEquals("newNodeValue", String(client.data.forPath("/path/for2/some/parent/path/newNode"), UTF_8))
        assertTrue(conflicts.isEmpty())
    }

    @Test
    fun testCreateZNodeCreatingParentsLogged() {
        curatorService.createZNode("/path/for2/some/parent/path", ZNode("newNode", "newNodeValue", false), false, user)

        assertEquals("newNodeValue", String(client.data.forPath("/path/for2/some/parent/path/newNode"), UTF_8))
        verify(logger).log(LoggedAction.Builder().action("create").server(connectionString).user(user).path("/path/for2/some/parent/path/newNode").newData("newNodeValue").build())
    }

    @Test
    fun testCreateZNodeConflicts() {
        val conflicts = curatorService.createZNode("/path/b", ZNode("c", "newCValue", false), false, user)

        assertEquals("cValue", String(client.data.forPath("/path/b/c"), UTF_8))
        assertEquals(1, conflicts.size)
    }

    @Test
    fun testCreateZNodeConflictsNoLogging() {
        curatorService.createZNode("/path/b", ZNode("c", "newCValue", false), false, user)
        verifyZeroInteractions(logger)
    }

    @Test
    fun testCreateZNodeOverwrite() {
        val conflicts = curatorService.createZNode("/path/b", ZNode("c", "newCValue", false), true, user)

        assertEquals("newCValue", String(client.data.forPath("/path/b/c"), UTF_8))
        assertTrue(conflicts.isEmpty())
    }

    @Test
    fun testCreateZNodeOverwriteLogging() {
        curatorService.createZNode("/path/b", ZNode("c", "newCValue", false), true, user)
        verify(logger).log(LoggedAction.Builder().action("create").server(connectionString).user(user).path("/path/b/c").newData("newCValue").build())
    }

    @Test
    fun testImport() {
        val node = ZNode("imported", "importedValue", true, listOf(
                ZNode("node2", "node2Value", false),
                ZNode("node1", "node1Value", true, listOf(
                        ZNode("node1child1", "node1child1Value", false),
                        ZNode("node1child2", "node1child2Value", false),
                        ZNode("node1child3", "node1child3Value", false)))
        ))

        val conflicts = curatorService.createZNode("/path/for2", node, false, user)

        assertEquals("importedValue", String(client.data.forPath("/path/for2/imported"), UTF_8))
        assertEquals("node1Value", String(client.data.forPath("/path/for2/imported/node1"), UTF_8))
        assertEquals("node2Value", String(client.data.forPath("/path/for2/imported/node2"), UTF_8))
        assertEquals("node1child1Value", String(client.data.forPath("/path/for2/imported/node1/node1child1"), UTF_8))
        assertEquals("node1child2Value", String(client.data.forPath("/path/for2/imported/node1/node1child2"), UTF_8))
        assertEquals("node1child3Value", String(client.data.forPath("/path/for2/imported/node1/node1child3"), UTF_8))
        assertTrue(conflicts.isEmpty())
    }

    @Test
    fun testImportConflicts() {
        // создаем 2 ноды
        val node = ZNode("imported", "importedValue", true, listOf(
                ZNode("node2", "node2Value", false)
        ))
        curatorService.createZNode("/path/for2", node, false, user)

        //изменяем уже созданные, добавляем детей, создаем снова
        val node2 = node.copy(value = "overwrittenImportedValue", children = listOf(
                ZNode("node2", "overwrittenNode2Value", false),
                ZNode("node1", "node1Value", true, listOf(
                        ZNode("node1child1", "node1child1Value", false),
                        ZNode("node1child2", "node1child2Value", false),
                        ZNode("node1child3", "node1child3Value", false)
                ))
        ))

        val conflicts = curatorService.createZNode("/path/for2", node2, false, user)

        assertEquals("importedValue", String(client.data.forPath("/path/for2/imported"), UTF_8))
        assertEquals("node1Value", String(client.data.forPath("/path/for2/imported/node1"), UTF_8))
        assertEquals("node2Value", String(client.data.forPath("/path/for2/imported/node2"), UTF_8))
        assertEquals("node1child1Value", String(client.data.forPath("/path/for2/imported/node1/node1child1"), UTF_8))
        assertEquals("node1child2Value", String(client.data.forPath("/path/for2/imported/node1/node1child2"), UTF_8))
        assertEquals("node1child3Value", String(client.data.forPath("/path/for2/imported/node1/node1child3"), UTF_8))
        assertEquals(2, conflicts.size)
    }

    @Test
    fun testImportOverwrite() {
        // создаем 2 ноды
        val node = ZNode("imported", "importedValue", true, listOf(
                ZNode("node2", "node2Value", false)
        ))
        curatorService.createZNode("/path/for2", node, false, user)

        //изменяем уже созданные, добавляем детей, создаем снова
        val node2 = node.copy(value = "overwrittenImportedValue", children = listOf(
                ZNode("node2", "overwrittenNode2Value", false),
                ZNode("node1", "node1Value", true, listOf(
                        ZNode("node1child1", "node1child1Value", false),
                        ZNode("node1child2", "node1child2Value", false),
                        ZNode("node1child3", "node1child3Value", false)
                ))
        ))

        val conflicts = curatorService.createZNode("/path/for2", node2, true, user)

        assertEquals("overwrittenImportedValue", String(client.data.forPath("/path/for2/imported"), UTF_8))
        assertEquals("node1Value", String(client.data.forPath("/path/for2/imported/node1"), UTF_8))
        assertEquals("overwrittenNode2Value", String(client.data.forPath("/path/for2/imported/node2"), UTF_8))
        assertEquals("node1child1Value", String(client.data.forPath("/path/for2/imported/node1/node1child1"), UTF_8))
        assertEquals("node1child2Value", String(client.data.forPath("/path/for2/imported/node1/node1child2"), UTF_8))
        assertEquals("node1child3Value", String(client.data.forPath("/path/for2/imported/node1/node1child3"), UTF_8))
        assertTrue(conflicts.isEmpty())
    }

    /**
     * zookeeper refuses connection if single message is larger than a megabyte
     * that'user why can't use transactional API
     */
    @Test
    fun testImportMoreThanMegabyte() {
        val headersAndStuffApproxSize = 256
        val megabyte = ByteArray((1024 * 1024) - headersAndStuffApproxSize) { 1 }

        val twoMegabytes = (1..2).map { i ->
            ZNode(i.toString(), String(megabyte))
        }

        val hugeZNode = ZNode("stress", children = twoMegabytes)

        curatorService.createZNode("/", hugeZNode, false, user)
    }

}
