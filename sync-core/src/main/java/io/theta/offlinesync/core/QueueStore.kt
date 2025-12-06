package io.theta.offlinesync.core

/**
 * Durable queue interface. Implementations (Room/SQLDelight) will persist operations.
 */
interface QueueStore {

    suspend fun enqueue(operation: Operation)

    suspend fun peek(limit: Int = 10): List<Operation>

    suspend fun markSuccess(operationId: String)

    suspend fun markFailed(
        operationId: String,
        error: String,
        retryable: Boolean
    )

    suspend fun updateAttempts(
        opId: String,
        attempts: Int
    )

    suspend fun getPendingCount(): Long

    suspend fun getById(operationId: String): Operation?

    suspend fun remove(operationId: String)

}