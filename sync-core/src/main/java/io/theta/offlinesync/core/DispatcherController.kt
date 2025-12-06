package io.theta.offlinesync.core

import io.theta.offlinesync.core.utils.BackoffPolicy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class SyncEngineImpl(
    private val queueStore: QueueStore,
    private val networkAdapter: NetworkAdapter,
    private val scheduler: Scheduler?,
    private val conflictStrategy: ConflictStrategy?,
    private val backoffPolicy: BackoffPolicy
) : SyncEngine {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Volatile
    private var running = false

    private val _state = MutableStateFlow(SyncState(SyncStatus.IDLE, 0))

    override fun state(): Flow<SyncState> = _state.asStateFlow()

    // simple in-memory channel for queue observation
    private val _queueFlow = MutableStateFlow<List<Operation>>(emptyList())

    override fun observeQueue() = _queueFlow.asStateFlow()

    override suspend fun enqueue(operation: Operation) {
        queueStore.enqueue(operation)
        refreshQueueSnapshot()
    }

    override suspend fun runNow() {
        dispatchOnce()
    }

    override fun start() {
        if (running)
            return

        running = true

        scheduler?.schedulePeriodicSync()
        scope.launch { loopDispatch() }
    }

    override fun stop() {
        running = false
        scope.cancel()
    }

    override suspend fun forceRetry(operationId: String) {
        val op = queueStore.getById(operationId) ?: return
        queueStore.updateAttempts(op.id, 0)
        dispatchOnce()
    }

    private suspend fun refreshQueueSnapshot() {
        val count = queueStore.getPendingCount().toInt()
        _state.value = _state.value.copy(queueSize = count)
        _queueFlow.value = queueStore.peek(100)
    }

    private suspend fun loopDispatch() {
        while (running) {
            try {
                dispatchOnce()
            } catch (e: Exception) {

                // surface error
                _state.value = SyncState(
                    status = SyncStatus.ERROR, _state.value.queueSize,
                    lastError = e.message
                )

                // sleep a bit
                delay(5.seconds)
            }

            // small delay to avoid tight loop
            delay(1.seconds)
        }
    }

    private suspend fun dispatchOnce() {
        _state.value = _state.value.copy(
            status = SyncStatus.SYNCING
        )

        val pending = queueStore.peek(limit = 5)

        if (pending.isEmpty()) {
            _state.value = _state.value.copy(
                status = SyncStatus.IDLE
            )
            return
        }

        for (operation in pending) {

            val result = try {
                networkAdapter.execute(operation)
            } catch (e: Exception) {
                NetworkResult.Failure(
                    httpCode = null,
                    errorMessage = e.message ?: "Unknown error",
                    retryable = true
                )
            }

            when (result) {
                is NetworkResult.Success -> {
                    queueStore.markSuccess(operationId = operation.id)
                }

                is NetworkResult.Failure -> {
                    val retryable = result.retryable

                    queueStore.markFailed(
                        operationId = operation.id,
                        error = result.errorMessage,
                        retryable = retryable
                    )

                    if (retryable) {
                        val nextAttempts = operation.attempts + 1
                        queueStore.updateAttempts(operation.id, nextAttempts)
                    } else {
                        // permanent failure
                    }

                }
            }
            refreshQueueSnapshot()
        }

        _state.value = _state.value.copy(status = SyncStatus.IDLE)
    }
}