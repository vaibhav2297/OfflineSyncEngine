package io.theta.offlinesync.delta

/**
 * Represents a change to a resource.
 *
 * @property resource The type of resource that was changed.
 * @property resourceId The ID of the resource that was changed.
 * @property payloadJson The new state of the resource as a JSON string.
 * @property deleted Whether the resource was deleted.
 * @property version The version of the resource.
 */
data class Change(
    val resource: String,
    val resourceId: String,
    val payloadJson: String,
    val deleted: Boolean = false,
    val version: Long? = null
)
