package io.theta.offlinesync.delta

data class Change(
    val resource: String,
    val resourceId: String,
    val payloadJson: String,
    val deleted: Boolean = false,
    val version: Long? = null
)