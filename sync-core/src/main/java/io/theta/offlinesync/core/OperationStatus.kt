package io.theta.offlinesync.core

enum class OperationStatus {
    PENDING,
    IN_PROGRESS,
    FAILED,
    SUCCESS,
    CANCELLED
}

enum class OperationType {
    CREATE,
    UPDATE,
    DELETE,
    CUSTOM
}