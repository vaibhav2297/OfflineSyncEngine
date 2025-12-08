package io.theta.offlinesync.core

import io.theta.offlinesync.core.utils.BackoffPolicy
import kotlinx.coroutines.flow.Flow

/**
 * The main interface for the offline synchronization engine.
 * This interface provides methods for enqueuing operations, controlling the sync process,
 * and observing the state of the engine.
 */
interface SyncEngine {

    /**
     * Enqueues a new [Operation] to be synchronized with the server.
     *
     * @param operation The operation to be added to the queue.
     */
    suspend fun enqueue(operation: Operation)

    /**
     * Triggers an immediate, one-time synchronization attempt.
     */
    suspend fun runNow()

    /**
     * Forces a retry of a previously failed operation.
     *
     * @param operationId The ID of the operation to retry.
     */
    suspend fun forceRetry(operationId: String)

    /**
     * Returns a [Flow] that emits the current [SyncState] of the engine.
     * This can be used to observe the engine's status, queue size, and any errors.
     *
     * @return A [Flow] of [SyncState].
     */
    fun state(): Flow<SyncState>

    /**
     * Returns a [Flow] that emits the current list of pending operations in the queue.
     *
     * @return A [Flow] of a list of [Operation]s.
     */
    fun observeQueue(): Flow<List<Operation>>

    /**
     * Starts the synchronization engine. If a [Scheduler] is provided, it will
     * begin periodic synchronization.
     */
    fun start()

    /**
     * Stops the synchronization engine and cancels any ongoing operations.
     */
    fun stop()

    /**
     * A builder class for creating instances of [SyncEngine].
     *
     * @param queueStore The durable store for pending operations.
     * @param networkAdapter The adapter for making network requests.
     */
    class Builder(
        private val queueStore: QueueStore,
        private val networkAdapter: NetworkAdapter
    ) {

        private var scheduler: Scheduler? = null

        private var conflictStrategy: ConflictStrategy? = null

        private var backoffPolicy: BackoffPolicy = BackoffPolicy.default()

        /**
         * Sets the [Scheduler] for periodic synchronization.
         */
        fun scheduler(s: Scheduler) = apply { this.scheduler = s }

        /**
         * Sets the [ConflictStrategy] for resolving data conflicts.
         */
        fun conflictStrategy(c: ConflictStrategy) = apply { this.conflictStrategy = c }

        /**
         * Sets the [BackoffPolicy] for retrying failed operations.
         */
        fun backoffPolicy(b: BackoffPolicy) = apply { this.backoffPolicy = b }

        /**
         * Builds and returns a [SyncEngine] instance with the configured parameters.
         */
        fun build(): SyncEngine {
            return SyncEngineImpl(
                queueStore = queueStore,
                networkAdapter = networkAdapter,
                scheduler = scheduler,
                conflictStrategy = conflictStrategy,
                backoffPolicy = backoffPolicy
            )
        }
    }
}
