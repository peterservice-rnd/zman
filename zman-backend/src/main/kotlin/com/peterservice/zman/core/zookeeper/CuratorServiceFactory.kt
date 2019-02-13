package com.peterservice.zman.core.zookeeper

import com.peterservice.zman.api.entities.ZServer
import com.peterservice.zman.api.exceptions.ZManException
import com.peterservice.zman.api.exceptions.ZookeeperConnectionException
import com.peterservice.zman.core.zookeeper.events.ActionListener
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.RetryNTimes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
internal class CuratorServiceFactory() {

    @Value("\${zman.curator.retries-count:2}")
    private var retries: Int = 2
    @Value("\${zman.curator.sleep-time-between-retries-millis:1000}")
    private var sleepTimeBetweenRetries: Int = 1000

    @Autowired(required = false)
    var listenerServices: Array<ActionListener> = emptyArray()

    fun create(server: ZServer): CuratorService {
        val client = createCuratorClient(server.connectionString)
        return CuratorService(client, *listenerServices)
    }

    private fun createCuratorClient(connectionString: String): CuratorFramework {
        val client = CuratorFrameworkFactory.newClient(connectionString, RetryNTimes(retries, sleepTimeBetweenRetries))
        client.start()

        val connected = waitUntilConnectedOrTimedOut(client)

        if (!connected) {
            client.close()
            throw ZookeeperConnectionException("Could not connect to server: " + connectionString)
        }

        return client
    }

    private fun waitUntilConnectedOrTimedOut(client: CuratorFramework): Boolean {
        try {
            return client.blockUntilConnected(retries * sleepTimeBetweenRetries, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            throw ZManException(e)
        }

    }

}
