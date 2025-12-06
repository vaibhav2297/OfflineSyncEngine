package io.theta.offlinesync.core

interface Scheduler {

    fun requestImmediateSync()

    fun schedulePeriodicSync()
}