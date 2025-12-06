package io.theta.offlinesync.core

import io.theta.offlinesync.core.utils.BackoffPolicy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class SyncEngineImpl(
    private val queueStore: QueueStore,
    private val networkAdapter: NetworkAdapter,
    private val scheduler: Scheduler?,
    private val conflictStrategy: ConflictStrategy?,
    private val backoffPolicy: BackoffPolicy
): SyncEngine {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow(SyncState(SyncStatus.IDLE, 0))

    override suspend fun enqueue(operation: Operation) {
        TODO("Not yet implemented")
    }

    override suspend fun runNow() {
        TODO("Not yet implemented")
    }

    override suspend fun forceRetry(operationId: String) {
        TODO("Not yet implemented")
    }

    override fun state(): Flow<List<SyncState>> {
        TODO("Not yet implemented")
    }

    override fun observeQueue(): Flow<List<Operation>> {
        TODO("Not yet implemented")
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

}