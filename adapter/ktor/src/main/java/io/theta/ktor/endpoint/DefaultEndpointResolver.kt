package io.theta.ktor.endpoint

import io.theta.offlinesync.core.Operation
import io.theta.offlinesync.core.OperationType

/**
 * A [EndpointResolver] that resolves endpoints based on the [OperationType].
 *
 * @property baseUrl The base URL to use for resolving endpoints.
 */
class DefaultEndpointResolver(
    private val baseUrl: String
) : EndpointResolver {

    /**
     * Resolves the endpoint for the given [operation].
     *
     * @param operation The [Operation] to resolve the endpoint for.
     * @return The resolved endpoint.
     */
    override fun resolve(operation: Operation): String {
        return when (operation.type) {
            OperationType.CREATE -> "$baseUrl/${operation.resource}"

            OperationType.UPDATE,
            OperationType.DELETE -> "$baseUrl/${operation.resource}/${operation.resourceId}"

            else -> "$baseUrl/${operation.resource}"
        }
    }
}