package io.theta.offlinesync.core

/**
 * Represents the current state of the [SyncEngine].
 *
 * @property status The current synchronization status.
 * @property queueSize The number of pending operations in the queue.
 * @property lastSyncTimeEpochMs The timestamp of the last successful synchronization, in milliseconds
 *                             since the epoch.
 * @property lastError A message describing the last error that occurred, if any.
 */
data class SyncState(
    val status: SyncStatus,
    val queueSize: Int,
    val lastSyncTimeEpochMs: Long? = null,
    val lastError: String? = null
)

/**
 * Represents the synchronization status of the [SyncEngine].
 */
enum class SyncStatus {
    /** The engine is idle and not currently synchronizing. */
    IDLE,
    /** The engine is actively synchronizing with the server. */
    SYNCING,
    /** An error occurred during the last synchronization attempt. */
    ERROR
}
