package io.theta.offlinesync.core

/**
 * An interface for a durable queue that stores [Operation] objects.
 * Implementations of this interface, such as those using Room or SQLDelight,
 * are responsible for persisting sync operations to ensure they are not lost
 * if the application closes.
 */
interface QueueStore {

    /**
     * Adds an [Operation] to the queue.
     *
     * @param operation The operation to be enqueued.
     */
    suspend fun enqueue(operation: Operation)

    /**
     * Retrieves a list of pending operations from the front of the queue
     * without removing them.
     *
     * @param limit The maximum number of operations to retrieve.
     * @return A list of the pending [Operation]s.
     */
    suspend fun peek(limit: Int = 10): List<Operation>

    /**
     * Marks an operation as successfully completed, and removes it from the queue.
     *
     * @param operationId The ID of the operation to mark as successful.
     */
    suspend fun markSuccess(operationId: String)

    /**
     * Marks an operation as failed.
     *
     * @param operationId The ID of the operation that failed.
     * @param error A message describing the reason for the failure.
     * @param retryable A boolean indicating whether the operation can be retried later.
     */
    suspend fun markFailed(
        operationId: String,
        error: String,
        retryable: Boolean
    )

    /**
     * Updates the retry attempt count for a specific operation.
     *
     * @param opId The ID of the operation to update.
     * @param attempts The new attempt count.
     */
    suspend fun updateAttempts(
        opId: String,
        attempts: Int
    )

    /**
     * Returns the total number of pending operations in the queue.
     *
     * @return The number of pending operations.
     */
    suspend fun getPendingCount(): Long

    /**
     * Retrieves a specific operation by its ID.
     *
     * @param operationId The ID of the operation to retrieve.
     * @return The [Operation] if found, or `null` otherwise.
     */
    suspend fun getById(operationId: String): Operation?

    /**
     * Removes an operation from the queue.
     *
     * @param operationId The ID of the operation to remove.
     */
    suspend fun remove(operationId: String)

}
