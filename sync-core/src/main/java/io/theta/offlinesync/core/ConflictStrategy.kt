package io.theta.offlinesync.core

/**
 * A conflict resolution strategy. App can provide implementations.
 */
interface ConflictStrategy {

    suspend fun resolve(
        localJson: String,
        serverJson: String
    ): String
}