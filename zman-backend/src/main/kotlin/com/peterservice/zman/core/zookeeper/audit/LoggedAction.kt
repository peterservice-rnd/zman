package com.peterservice.zman.core.zookeeper.audit

data class LoggedAction(
        val server: String,
        val action: String,
        val path: String,
        val user: String?,
        val newData: String?,
        val oldData: String?
) {
    private constructor(builder: Builder) : this(builder.server, builder.action, builder.path, builder.user, builder.newData, builder.oldData)

    fun isEmpty() : Boolean = action.isEmpty()

    class Builder {
        var server: String = ""
            private set
        var action: String = ""
            private set
        var path: String = ""
            private set
        var user: String? = ""
            private set
        var oldData: String? = null
            private set
        var newData: String? = null
            private set

        fun server(server: String) = apply { this.server = server }
        fun action(action: String) = apply { this.action = action }
        fun path(path: String) = apply { this.path = path }
        fun user(user: String?) = apply { this.user = user }
        fun newData(newData: String?) = apply { this.newData = newData }
        fun oldData(oldData: String?) = apply { this.oldData = oldData }

        fun build() = LoggedAction(this)
    }
}
