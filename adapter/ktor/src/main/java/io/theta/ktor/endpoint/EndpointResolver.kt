package io.theta.ktor.endpoint

import io.theta.offlinesync.core.Operation

/**
 * An interface for resolving endpoints for a given [Operation].
 */
interface EndpointResolver {

    /**
     * Resolves the endpoint for the given [operation].
     *
     * @param operation The [Operation] to resolve the endpoint for.
     * @return The resolved endpoint.
     */
    fun resolve(operation: Operation): String
}