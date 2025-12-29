package io.theta.offlinesync.delta

interface ChangeApplier {

    suspend fun apply(changes: List<Change>)
}