package io.theta.offlinesync.core.utils

import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class BackoffPolicy(
    val baseDelaySecond: Long = 2,
    val maxDelaySecond: Long = 120,
    val maxAttempts: Long = 5,
) {

    fun nextDelay(attempts: Int): Duration {
        val exp = 2.0.pow(attempts.toDouble()).toLong()
        val secs = (baseDelaySecond * exp).coerceAtMost(maxDelaySecond)
        return secs.seconds
    }

    companion object {
        fun default() = BackoffPolicy()
    }
}