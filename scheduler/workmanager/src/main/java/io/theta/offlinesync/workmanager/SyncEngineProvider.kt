package io.theta.offlinesync.workmanager

import io.theta.offlinesync.core.SyncEngine

/**
 * A singleton object that provides a [SyncEngine] instance.
 */
object SyncEngineProvider {
    /**
     * The [SyncEngine] instance.
     */
    var engine: SyncEngine? = null
}