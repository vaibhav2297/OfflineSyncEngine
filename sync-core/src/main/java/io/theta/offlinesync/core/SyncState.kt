package io.theta.offlinesync.core

data class SyncState(
    val status: SyncStatus,
    val queueSize: Int,
    val lastSyncTimeEpochMs: Long? = null,
    val lastError: String? = null
)

enum class SyncStatus { IDLE, SYNCING, ERROR }