package io.theta.offlinesync.room

import io.theta.offlinesync.core.Operation
import io.theta.offlinesync.core.QueueStore
import io.theta.offlinesync.room.dao.QueueDao
import io.theta.offlinesync.room.utils.extensions.toEntity
import io.theta.offlinesync.room.utils.extensions.toOperation

class RoomQueueStore(
    private val queueDao: QueueDao
) : QueueStore {

    override suspend fun enqueue(operation: Operation) {
        queueDao.insert(operation.toEntity())
    }

    override suspend fun peek(limit: Int): List<Operation> {
        val list = queueDao.peekPending(limit)
        return list.map { it.toOperation() }
    }

    override suspend fun markSuccess(operationId: String) {
        queueDao.markSuccessAndRemove(operationId)
    }

    override suspend fun markFailed(
        operationId: String,
        error: String,
        retryable: Boolean
    ) {
        // For simplicity: set status to FAILED; if retryable keep as FAILED so it is retried by dispatcher
        val status = if (retryable) "FAILED" else "CANCELLED"
        queueDao.markFailed(operationId, status, error)
    }

    override suspend fun updateAttempts(opId: String, attempts: Int) {
        queueDao.updateAttempts(opId, attempts)
    }

    override suspend fun getPendingCount(): Long = queueDao.countPending()

    override suspend fun getById(operationId: String): Operation? =
        queueDao.getById(operationId)?.let { it.toOperation() }

    override suspend fun remove(operationId: String) {
        queueDao.deleteById(operationId)
    }
}