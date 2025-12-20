package io.theta.ktor.utils

import io.ktor.client.statement.HttpResponse

/**
 * A utility object for classifying HTTP errors.
 */
object ErrorClassifier {

    /**
     * Returns whether the given [response] is retryable.
     *
     * @param response The [HttpResponse] to classify.
     * @return `true` if the response is retryable, `false` otherwise.
     */
    fun isRetryable(response: HttpResponse): Boolean =
        response.status.value >= 500 || response.status.value == 429
}