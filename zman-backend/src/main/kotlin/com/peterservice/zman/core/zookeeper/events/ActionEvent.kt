package com.peterservice.zman.core.zookeeper.events

data class ActionEvent(
        val user: String?,
        val server: String,
        val actions: List<ActionItem>
) {

    data class ActionItem(val action: String,
        val path: String,
        val newData: String?,
        val oldData: String?)

    private constructor(builder: Builder) : this(builder.user, builder.server, builder.items)

    fun isEmpty() : Boolean = actions.isEmpty()

    class Builder {
        class ItemBuilder(var parent: Builder, val newAction: String) {
            var path: String = ""
                private set
            var oldData: String? = null
                private set
            var newData: String? = null
                private set

            fun path(path: String) = apply { this.path = path }
            fun newData(newData: String?) = apply { this.newData = newData }
            fun oldData(oldData: String?) = apply { this.oldData = oldData }
            fun add() : Builder {
                parent.items += ActionItem(newAction, path, newData, oldData)
                return parent
            }
        }

        var server: String = ""
            private set
        var user: String? = ""
            private set
        var items: List<ActionItem> = ArrayList<ActionItem>()

        fun server(server: String) = apply { this.server = server }
        fun user(user: String?) = apply { this.user = user }
        fun action(action: String) : ItemBuilder = ItemBuilder(this, action)

        fun build() = ActionEvent(this)
    }
}
