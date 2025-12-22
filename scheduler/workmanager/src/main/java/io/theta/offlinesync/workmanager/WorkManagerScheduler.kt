package io.theta.offlinesync.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import io.theta.offlinesync.core.Scheduler
import java.util.concurrent.TimeUnit

/**
 * A [Scheduler] that uses [WorkManager] to schedule sync operations.
 *
 * @property workManager The [WorkManager] instance.
 */
class WorkManagerScheduler(
    context: Context
): Scheduler {

    private val workManager = WorkManager.getInstance(context)

    /**
     * Requests an immediate sync operation.
     */
    override fun requestImmediateSync() {
        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(defaultConstraints())
            .addTag(SYNC_WORK_TAG)
            .build()

        workManager.enqueueUniqueWork(
            uniqueWorkName = UNIQUE_IMMEDIATE_WORK,
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    /**
     * Schedules a periodic sync operation.
     */
    override fun schedulePeriodicSync() {
        val request = PeriodicWorkRequestBuilder<SyncWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit =  TimeUnit.MINUTES
        )
            .setConstraints(defaultConstraints())
            .addTag(SYNC_WORK_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = UNIQUE_PERIODIC_WORK,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun defaultConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    companion object {
        private const val UNIQUE_IMMEDIATE_WORK = "offline_sync_immediate"
        private const val UNIQUE_PERIODIC_WORK = "offline_sync_periodic"
        private const val SYNC_WORK_TAG = "offline_sync"
    }
}