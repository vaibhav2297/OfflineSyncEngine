package io.theta.offlinesync.core

/**
 * A sealed class representing the result of a network operation.
 * It can be either a [Success] or a [Failure].
 */
sealed class NetworkResult {

    /**
     * Represents a successful network operation.
     *
     * @property responsePayloadJson An optional JSON string containing the response from the server.
     * @property serverChangeToken An optional token that can be used for subsequent sync operations.
     */
    data class Success(
        val responsePayloadJson: String?,
        val serverChangeToken: String? = null
    ) : NetworkResult()

    /**
     * Represents a failed network operation.
     *
     * @property httpCode The HTTP status code of the response, if available.
     * @property errorMessage A message describing the error.
     * @property retryable A boolean indicating whether the operation can be retried.
     */
    data class Failure(
        val httpCode: Int?,
        val errorMessage: String,
        val retryable: Boolean
    ) : NetworkResult()
}
