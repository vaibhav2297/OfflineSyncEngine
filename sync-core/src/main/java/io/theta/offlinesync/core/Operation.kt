package io.theta.offlinesync.core

import kotlinx.serialization.Serializable

/**
 * Represents an outbound operation queued for sync.
 */
@Serializable
data class Operation(
    val id: String,
    val resource: String,
    val resourceId: String?,
    val type: OperationType,
    val payloadJson: String,
    val idempotencyKey: String,
    val createdAtEpochMs: Long,
    val priority: Int = 0,
    val attempts: Int = 0,
    val lastError: String? = null,
    val operationStatus: OperationStatus = OperationStatus.PENDING
)