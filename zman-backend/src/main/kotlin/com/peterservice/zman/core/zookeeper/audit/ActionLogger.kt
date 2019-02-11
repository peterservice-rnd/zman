package com.peterservice.zman.core.zookeeper.audit

interface ActionLogger {
    fun log(action: LoggedAction)
}