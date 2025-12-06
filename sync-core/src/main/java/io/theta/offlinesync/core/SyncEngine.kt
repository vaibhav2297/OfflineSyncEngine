package io.theta.offlinesync.core

import io.theta.offlinesync.core.utils.BackoffPolicy
import kotlinx.coroutines.flow.Flow

interface SyncEngine {

    suspend fun enqueue(operation: Operation)

    suspend fun runNow()

    suspend fun forceRetry(operationId: String)

    fun state(): Flow<SyncState>

    fun observeQueue(): Flow<List<Operation>>

    fun start()

    fun stop()

    class Builder(
        private val queueStore: QueueStore,
        private val networkAdapter: NetworkAdapter
    ) {

        private var scheduler: Scheduler? = null

        private var conflictStrategy: ConflictStrategy? = null

        private var backoffPolicy: BackoffPolicy = BackoffPolicy.default()

        fun scheduler(s: Scheduler) = apply { this.scheduler = s }

        fun conflictStrategy(c: ConflictStrategy) = apply { this.conflictStrategy = c }

        fun backoffPolicy(b: BackoffPolicy) = apply { this.backoffPolicy = b }

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