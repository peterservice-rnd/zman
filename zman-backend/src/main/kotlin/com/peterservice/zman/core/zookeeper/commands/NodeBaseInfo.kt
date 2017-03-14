package com.peterservice.zman.core.zookeeper.commands

import org.apache.zookeeper.data.Stat

data class NodeBaseInfo(
        val path: String,
        val data: ByteArray?,
        val stat: Stat? = null
)
