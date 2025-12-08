package io.theta.offlinesync.core

/**
 * The `NetworkAdapter` interface abstracts the underlying transport layer (e.g., HTTP, GraphQL)
 * for the sync engine. Implementations of this interface are responsible for mapping an [Operation]
 * to a network request and returning a [NetworkResult].
 */
interface NetworkAdapter {

    /**
     * Executes a given [Operation] by making a network request.
     *
     * @param operation The operation to be executed.
     * @return A [NetworkResult] indicating the outcome of the operation.
     */
    suspend fun execute(operation: Operation): NetworkResult

    /**
     * Fetches changes from the server that have occurred since a specific point in time,
     * identified by the `sinceToken`.
     *
     * @param sinceToken A token representing the last successful sync time. If null, a full
     *                   fetch should be performed.
     * @return A [FetchResult] containing the fetched changes and a new token for the next sync.
     */
    suspend fun fetchChanges(sinceToken: String?): FetchResult

    /**
     * Indicates whether the network adapter supports idempotency for operations.
     * Idempotent operations can be safely retried without causing unintended side effects.
     *
     * @return `true` if the adapter supports idempotency, `false` otherwise.
     */
    fun supportsIdempotency(): Boolean

}
