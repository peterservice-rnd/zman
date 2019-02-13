package com.peterservice.zman.core.zookeeper.events

interface ActionListener {
    fun handle(actionEvent: ActionEvent)
}