package com.peterservice.zman.api.entities

import com.fasterxml.jackson.annotation.JsonProperty

data class ZNode(

        @get:JsonProperty("znodeKey")
        val key: String,

        @get:JsonProperty("znodeValue")
        val value: String?,

        val hasChildren: Boolean,

        val children: List<ZNode> = emptyList(),

        /* for backward compatibility */
        @get:JsonProperty("znodeName")
        val path: String? = null
)
