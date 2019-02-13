package com.peterservice.zman.core.zookeeper.events

import org.junit.Assert.*
import org.junit.Test

class ActionEventTest {

    @Test
    fun testEmptyEvent() {
        var event = ActionEvent.Builder().server("server").user("user").build()
        assertTrue(event.isEmpty())
    }

    @Test
    fun testEqualsForSeveralEmptyEvents() {
        var event1 = ActionEvent.Builder().server("server").user("user")
        var event2 = ActionEvent.Builder().server("server").user("user")
        assertEquals(event2.build(), event1.build())
    }

    @Test
    fun testEqualsForSeveralEventsItems() {
        var event1 = ActionEvent.Builder().server("server").user("user")
        event1.action("delete").path("/path/a").add()
        event1.action("delete").path("/path/b").add()

        var event2 = ActionEvent.Builder().server("server").user("user")
        event2.action("delete").path("/path/a").add()
        event2.action("delete").path("/path/b").add()

        assertEquals(event2.build(), event1.build())
    }

    @Test
    fun testEqualsForSeveralNotEqualEventsItems() {
        var event1 = ActionEvent.Builder().server("server").user("user")
        event1.action("delete").path("/path/a").add()
        event1.action("delete").path("/path/b").add()

        var event2 = ActionEvent.Builder().server("server").user("user")
        event2.action("delete").path("/path/a").add()
        event2.action("delete").path("/path/b").oldData("x").add()

        assertNotEquals(event2.build(), event1.build())
    }
}