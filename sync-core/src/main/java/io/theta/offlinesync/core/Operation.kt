package io.theta.offlinesync.core

import kotlinx.serialization.Serializable

/**
 * Represents an outbound operation queued for synchronization.
 *
 * @property id A unique identifier for the operation.
 * @property resource The type of resource this operation affects (e.g., "user", "post").
 * @property resourceId The unique identifier of the resource, if applicable.
 * @property type The type of operation to be performed.
 * @property payloadJson A JSON string representing the data for the operation.
 * @property idempotencyKey A key to ensure that the operation is idempotent, preventing duplicate
 *                        processing on the server.
 * @property createdAtEpochMs The timestamp of when the operation was created, in milliseconds
 *                          since the epoch.
 * @property priority The priority of the operation, with higher numbers indicating higher priority.
 * @property attempts The number of times this operation has been attempted.
 * @property lastError A message describing the last error that occurred during an attempt, if any.
 * @property operationStatus The current status of the operation.
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
