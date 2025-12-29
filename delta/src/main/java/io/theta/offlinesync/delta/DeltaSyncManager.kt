package io.theta.offlinesync.delta

import io.theta.offlinesync.core.ConflictStrategy
import io.theta.offlinesync.core.NetworkAdapter
import io.theta.offlinesync.core.QueueStore

/**
 * Manages the delta sync process.
 *
 * @property networkAdapter The [NetworkAdapter] to use for fetching changes.
 * @property queueStore The [QueueStore] to use for accessing pending operations.
 * @property tokenStore The [ChangeTokenStore] to use for storing and retrieving the sync token.
 * @property changeApplier The [ChangeApplier] to use for applying changes.
 * @property conflictStrategy The [ConflictStrategy] to use for resolving conflicts.
 */
class DeltaSyncManager(
    private val networkAdapter: NetworkAdapter,
    private val queueStore: QueueStore,
    private val tokenStore: ChangeTokenStore,
    private val changeApplier: ChangeApplier,
    private val conflictStrategy: ConflictStrategy?
) {

    /**
     * Performs a delta sync.
     */
    suspend fun sync() {

        // Retrieving token
        val lastToken = tokenStore.getLastToken()

        // Fetch changes
        val fetchResult = networkAdapter.fetchChanges(lastToken)
        val changes = parseChanges(fetchResult.changeJson)

        if (changes.isEmpty()) {
            fetchResult.newSinceToken?.let { tokenStore.saveToken(it) }
            return
        }

        val pendingOps = queueStore.peek(limit = Int.MAX_VALUE)
        val (conflicted, nonConflicted) = changes.partition { change ->
            ConflictDetector.hasConflict(change, pendingOps)
        }

        // Apply non-conflicting changes directly
        if (nonConflicted.isNotEmpty()) {
            changeApplier.apply(nonConflicted)
        }

        // Handle conflicts
        if (conflicted.isNotEmpty() && conflictStrategy != null) {
            resolveConflicts(conflicted)
        }

        fetchResult.newSinceToken?.let {
            tokenStore.saveToken(it)
        }
    }

    private suspend fun resolveConflicts(conflicts: List<Change>) {
        for (change in conflicts) {
            val localOp = queueStore.peek(Int.MAX_VALUE).firstOrNull {
                it.resource == change.resource &&
                        it.resourceId == change.resourceId
            } ?: continue

            val resolvedJson = conflictStrategy?.resolve(
                localJson = localOp.payloadJson,
                serverJson = change.payloadJson
            ) ?: ""

            // Apply resolved result
            changeApplier.apply(
                listOf(
                    change.copy(payloadJson = resolvedJson)
                )
            )

            // Remove local pending op since conflict is resolved
            queueStore.remove(localOp.id)
        }
    }

    /**
     * Parses server JSON into list of Change objects.
     * This can be replaced by custom parser if needed.
     */
    private fun parseChanges(json: String): List<Change> {
        // Minimal placeholder (app may inject a parser later)
        // For now assume JSON parsing handled externally
        return emptyList()
    }
}