package io.theta.offlinesync.room.utils.extensions

import io.theta.offlinesync.core.Operation
import io.theta.offlinesync.room.entity.QueueEntity

internal fun QueueEntity.toOperation(): Operation {
    return Operation(
        id = this.id,
        resource = this.resource,
        resourceId = this.resourceId,
        type = when (this.type) {
            "CREATE" -> io.theta.offlinesync.core.OperationType.CREATE
            "UPDATE" -> io.theta.offlinesync.core.OperationType.UPDATE
            "DELETE" -> io.theta.offlinesync.core.OperationType.DELETE
            else -> io.theta.offlinesync.core.OperationType.CUSTOM
        },
        payloadJson = this.payloadJson,
        idempotencyKey = this.idempotencyKey,
        createdAtEpochMs = this.createdAtEpochMs,
        priority = this.priority,
        attempts = this.attempts,
        lastError = this.lastError,
        operationStatus = when (this.status) {
            "PENDING" -> io.theta.offlinesync.core.OperationStatus.PENDING
            "IN_PROGRESS" -> io.theta.offlinesync.core.OperationStatus.IN_PROGRESS
            "FAILED" -> io.theta.offlinesync.core.OperationStatus.FAILED
            "SUCCESS" -> io.theta.offlinesync.core.OperationStatus.SUCCESS
            "CANCELLED" -> io.theta.offlinesync.core.OperationStatus.CANCELLED
            else -> io.theta.offlinesync.core.OperationStatus.PENDING
        }
    )
}

internal fun Operation.toEntity(): QueueEntity {
    return QueueEntity(
        id = this.id,
        resource = this.resource,
        resourceId = this.resourceId,
        type = when (this.type) {
            io.theta.offlinesync.core.OperationType.CREATE -> "CREATE"
            io.theta.offlinesync.core.OperationType.UPDATE -> "UPDATE"
            io.theta.offlinesync.core.OperationType.DELETE -> "DELETE"
            else -> "CUSTOM"
        },
        payloadJson = this.payloadJson,
        idempotencyKey = this.idempotencyKey,
        attempts = this.attempts,
        status = when (this.operationStatus) {
            io.theta.offlinesync.core.OperationStatus.PENDING -> "PENDING"
            io.theta.offlinesync.core.OperationStatus.IN_PROGRESS -> "IN_PROGRESS"
            io.theta.offlinesync.core.OperationStatus.FAILED -> "FAILED"
            io.theta.offlinesync.core.OperationStatus.SUCCESS -> "SUCCESS"
            io.theta.offlinesync.core.OperationStatus.CANCELLED -> "CANCELLED"
        },
        priority = this.priority,
        createdAtEpochMs = this.createdAtEpochMs,
        lastError = this.lastError,
        schemaVersion = 1
    )
}