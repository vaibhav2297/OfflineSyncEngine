package io.theta.offlinesync.delta

/**
 * An interface for applying a list of [Change]s to the local data source.
 */
interface ChangeApplier {

    /**
     * Applies the given list of [changes] to the local data source.
     *
     * @param changes The list of changes to apply.
     */
    suspend fun apply(changes: List<Change>)
}
