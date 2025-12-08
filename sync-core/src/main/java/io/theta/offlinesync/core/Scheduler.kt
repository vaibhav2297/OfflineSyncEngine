package io.theta.offlinesync.core

/**
 * An interface for scheduling synchronization tasks.
 * Implementations of this interface can be used to trigger syncs based on different
 * strategies, such as periodic intervals or immediate requests.
 */
interface Scheduler {

    /**
     * Requests an immediate, one-time synchronization.
     */
    fun requestImmediateSync()

    /**
     * Schedules a periodic synchronization task. The interval and other scheduling
     * parameters are determined by the specific implementation.
     */
    fun schedulePeriodicSync()
}
