package io.theta.offlinesync.core.utils

import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Defines the backoff strategy for retrying failed operations.
 *
 * @property baseDelaySecond The initial delay in seconds before the first retry.
 * @property maxDelaySecond The maximum delay in seconds between retries.
 * @property maxAttempts The maximum number of retry attempts for an operation.
 */
data class BackoffPolicy(
    val baseDelaySecond: Long = 2,
    val maxDelaySecond: Long = 120,
    val maxAttempts: Long = 5,
) {

    /**
     * Calculates the delay for the next retry attempt using exponential backoff.
     * The delay is calculated as `baseDelaySecond * (2 ^ attempts)`.
     * The result is capped at `maxDelaySecond`.
     *
     * @param attempts The number of failed attempts so far.
     * @return The [Duration] to wait before the next attempt.
     */
    fun nextDelay(attempts: Int): Duration {
        val exp = 2.0.pow(attempts.toDouble()).toLong()
        val secs = (baseDelaySecond * exp).coerceAtMost(maxDelaySecond)
        return secs.seconds
    }

    companion object {
        /**
         * Creates a default [BackoffPolicy] instance.
         */
        fun default() = BackoffPolicy()
    }
}
