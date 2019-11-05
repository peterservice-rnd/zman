package com.peterservice.zman.core.mvc

import com.google.common.base.Charsets
import com.peterservice.zman.api.entities.ZServer
import com.peterservice.zman.api.repositories.AliasRepository
import com.peterservice.zman.api.zookeeper.ZookeeperServiceManager
import com.peterservice.zman.core.zookeeper.CuratorService
import com.peterservice.zman.core.zookeeper.CuratorServiceFactory
import com.peterservice.zman.core.zookeeper.events.ActionListener
import junit.framework.TestCase
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.RetryNTimes
import org.apache.curator.test.TestingServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.validation.constraints.NotNull

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = arrayOf("authentication.type: NONE", "spring.datasource.url: jdbc:h2:mem:zk"))
class ZNodeExportTest {
    @Autowired
    @NotNull
    lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var mockAliasRepository: AliasRepository

    @MockBean
    private lateinit var mockZookeeperServiceManager: ZookeeperServiceManager

    private lateinit var curatorService: CuratorService
    private lateinit var connectionString: String
    private lateinit var client: CuratorFramework
    private lateinit var listener: ActionListener

    companion object {
        const val ALIAS_NAME = "test"
        const val HEADER_NAME = HttpHeaders.CONTENT_DISPOSITION
        const val HEADER_VALUE_JSON = "attachment; filename=path.json"
        const val HEADER_VALUE_XML = "attachment; filename=path.xml"
        const val CONTENT_TYPE_XML = "application/xml;charset=UTF-8"
    }

    @Before
    fun setUp() {
        listener = Mockito.mock(ActionListener::class.java)
        val testingServer = TestingServer(true)
        val curatorServiceFactory = CuratorServiceFactory()
        curatorServiceFactory.listenerServices = arrayOf(listener)
        connectionString = testingServer.connectString

        curatorService = curatorServiceFactory.create(ZServer().apply { this.connectionString = testingServer.connectString })

        client = CuratorFrameworkFactory.newClient(testingServer.connectString, RetryNTimes(1, 0))
        client.start()

        client.create().forPath("/path", "pathValue".toByteArray(Charsets.UTF_8))
        client.create().forPath("/path/a", "aValue".toByteArray(Charsets.UTF_8))
        client.create().forPath("/path/b", "bValue".toByteArray(Charsets.UTF_8))

        val server = ZServer().also {
            it.alias = ALIAS_NAME
            it.connectionString = connectionString
        }

        `when`(mockAliasRepository.findByAlias(ALIAS_NAME)).thenReturn(server)

        `when`(mockZookeeperServiceManager.getServiceFor(server)).thenReturn(curatorService)
    }

    @After
    fun tearDown() {
        client.close()
    }

    @Test
    fun testExportByAcceptHeaderJson() {
        val result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/zk/{alias}/path", ALIAS_NAME)
                .param("export", true.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()

        TestCase.assertEquals(result.response.contentType, MediaType.APPLICATION_JSON_UTF8_VALUE)

        TestCase.assertEquals(result.response.getHeader(HEADER_NAME), HEADER_VALUE_JSON)
    }

    @Test
    fun testExportByAcceptHeaderXML() {
        val result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/zk/{alias}/path", ALIAS_NAME)
                .param("export", true.toString())
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk)
                .andReturn()

        TestCase.assertEquals(result.response.contentType, CONTENT_TYPE_XML)

        TestCase.assertEquals(result.response.getHeader(HEADER_NAME), HEADER_VALUE_XML)
    }

    @Test
    fun testExportByParamJson() {
        val result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/zk/{alias}/path", ALIAS_NAME)
                .param("export", true.toString())
                .param("format", "json")
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk)
                .andReturn()

        TestCase.assertEquals(result.response.contentType, MediaType.APPLICATION_JSON_UTF8_VALUE)

        TestCase.assertEquals(result.response.getHeader(HEADER_NAME), HEADER_VALUE_JSON)
    }

    @Test
    fun testExportByParamXML() {
        val result = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/zk/{alias}/path", ALIAS_NAME)
                .param("export", true.toString())
                .param("format", "xml")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()

        TestCase.assertEquals(result.response.contentType, CONTENT_TYPE_XML)

        TestCase.assertEquals(result.response.getHeader(HEADER_NAME), HEADER_VALUE_XML)
    }
}