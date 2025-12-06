package io.theta.offlinesync.core

sealed class NetworkResult {

    data class Success(
        val responsePayloadJson: String?,
        val serverChangeToken: String? = null
    ) : NetworkResult()

    data class Failure(
        val httpCode: Int?,
        val errorMessage: String?,
        val retryable: Boolean
    ) : NetworkResult()
}
