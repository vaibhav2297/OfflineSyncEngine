package io.theta.offlinesync.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * A [CoroutineWorker] that runs the sync engine.
 *
 * @param context The application context.
 * @param params The worker parameters.
 */
class SyncWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {

    /**
     * The work to be performed.
     *
     * @return The result of the work.
     */
    override suspend fun doWork(): Result {
        return try {
            val engine = SyncEngineProvider.engine
                ?: return Result.failure()

            engine.runNow()
            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }
}