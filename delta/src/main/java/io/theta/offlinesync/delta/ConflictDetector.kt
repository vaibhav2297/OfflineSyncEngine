package io.theta.offlinesync.delta

import io.theta.offlinesync.core.Operation

object ConflictDetector {

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