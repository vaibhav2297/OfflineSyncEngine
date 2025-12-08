package io.theta.offlinesync.core

/**
 * Represents the status of a sync operation.
 */
enum class OperationStatus {
    /** The operation is waiting to be processed. */
    PENDING,
    /** The operation is currently being processed. */
    IN_PROGRESS,
    /** The operation has failed. */
    FAILED,
    /** The operation has completed successfully. */
    SUCCESS,
    /** The operation has been cancelled. */
    CANCELLED
}

/**
 * Represents the type of a sync operation.
 */
enum class OperationType {
    /** A create operation. */
    CREATE,
    /** An update operation. */
    UPDATE,
    /** A delete operation. */
    DELETE,
    /** A custom operation type. */
    CUSTOM
}
