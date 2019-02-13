package com.peterservice.zman.core.zookeeper.events

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.stereotype.Component

@Component
@ConditionalOnExpression("\${audit.enabled:true}")
class FileAuditLogger : ActionListener {
    val log = LoggerFactory.getLogger(FileAuditLogger::class.java)

    override fun handle(actionEvent: ActionEvent) {
        // samples:
        // alexey@localhost:2181 create /node/node = 'abc'
        // alexey@localhost:2181 delete /node/node = 'abc'
        // alexey@localhost:2181 update /node/node = 'def' -> 'abc'

        if (!actionEvent.isEmpty()) {
            actionEvent.actions.forEach {
                log.info("{}@{} {} {}{}", actionEvent.server, actionEvent.user, it.action, it.path, buildDataString(it))
            }
        }
    }

    private fun buildDataString(actionEventItem: ActionEvent.ActionItem): String {
        if (!actionEventItem.newData.isNullOrBlank() || !actionEventItem.oldData.isNullOrBlank()) {
            return if (actionEventItem.oldData.isNullOrBlank()) """ '${actionEventItem.newData?.replace("\\s","")}'""" else """ '${actionEventItem.oldData?.replace("\\s","")}' -> '${actionEventItem.newData?.replace("\\s","")}'"""
        } else {
            return ""
        }
    }
}