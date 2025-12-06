package io.theta.offlinesync.core

/**
 * Network adapter abstracts transport layer (HTTP/GraphQL/etc).
 * Implementations should map Operation -> network request and return NetworkResult.
 */
interface NetworkAdapter {

    suspend fun execute(operation: Operation): NetworkResult

    suspend fun fetchChanges(sinceToken: String?): FetchResult

    fun supportsIdempotency(): Boolean

}