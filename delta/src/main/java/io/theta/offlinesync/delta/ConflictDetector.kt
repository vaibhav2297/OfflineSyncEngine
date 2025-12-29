package io.theta.offlinesync.delta

import io.theta.offlinesync.core.Operation

/**
 * A utility object for detecting conflicts between incoming changes and pending operations.
 */
object ConflictDetector {

    /**
     * Returns whether the given [change] conflicts with any of the given [pendingOps].
     *
     * @param change The incoming change.
     * @param pendingOps The list of pending operations.
     * @return `true` if a conflict is detected, `false` otherwise.
     */
    fun hasConflict(
        change: Change,
        pendingOps: List<Operation>
    ): Boolean {
        return pendingOps.any { op ->
            op.resource == change.resource &&
                    op.resourceId == change.resourceId
        }
    }
}
