package com.peterservice.zman.core.zookeeper.audit

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class FileAuditLogger : ActionLogger {
    val log = LoggerFactory.getLogger(FileAuditLogger::class.java)

    override fun log(action: LoggedAction) {
        // samples:
        // alexey@localhost:2181 create /node/node = 'abc'
        // alexey@localhost:2181 read /node/node
        // alexey@localhost:2181 delete /node/node = 'abc'
        // alexey@localhost:2181 update /node/node = 'def' -> 'abc'

        if (!action.isEmpty() && ("read" != action.action || logReadOperations)) {
            log.info("{}@{} {} {}{}", action.server, action.user, action.action, action.path, buildDataString(action))
        }
    }

    private fun buildDataString(action: LoggedAction): String {
        if (!action.newData.isNullOrBlank() || !action.oldData.isNullOrBlank()) {
            return if (action.oldData.isNullOrBlank()) """ '${action.newData?.replace("\\s","")}'""" else """ '${action.oldData?.replace("\\s","")}' -> '${action.newData?.replace("\\s","")}'"""
        } else {
            return ""
        }
    }

    @Value("\${logging.level.auditReadCommands}")
    var logReadOperations: Boolean = false
}