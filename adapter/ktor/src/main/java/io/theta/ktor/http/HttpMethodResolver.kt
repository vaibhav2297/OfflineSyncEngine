package io.theta.ktor.http

import io.ktor.http.HttpMethod
import io.theta.offlinesync.core.OperationType

/**
 * A utility object for resolving the [HttpMethod] for a given [OperationType].
 */
object HttpMethodResolver {

    /**
     * Resolves the [HttpMethod] for the given [type].
     *
     * @param type The [OperationType] to resolve the [HttpMethod] for.
     * @return The resolved [HttpMethod].
     */
    fun resolve(type: OperationType): HttpMethod {
        return when (type) {
            OperationType.CREATE -> HttpMethod.Post
            OperationType.UPDATE -> HttpMethod.Put
            OperationType.DELETE -> HttpMethod.Delete
            OperationType.CUSTOM -> HttpMethod.Post
        }
    }
}