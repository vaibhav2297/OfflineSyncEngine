package io.theta.ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.theta.ktor.endpoint.EndpointResolver
import io.theta.ktor.http.HttpMethodResolver
import io.theta.ktor.utils.ErrorClassifier
import io.theta.offlinesync.core.FetchResult
import io.theta.offlinesync.core.NetworkAdapter
import io.theta.offlinesync.core.NetworkResult
import io.theta.offlinesync.core.Operation
import io.theta.offlinesync.core.OperationType

/**
 * A [NetworkAdapter] that uses Ktor to make network requests.
 *
 * @property client The [HttpClient] to use for making requests.
 * @property endpointResolver The [EndpointResolver] to use for resolving endpoints.
 * @property deltaSyncEndpoint The endpoint to use for fetching changes.
 */
class KtorNetworkAdapter(
    private val client: HttpClient,
    private val endpointResolver: EndpointResolver,
    private val deltaSyncEndpoint: String? = null
) : NetworkAdapter {

    /**
     * Executes the given [operation] and returns a [NetworkResult].
     *
     * @param operation The [Operation] to execute.
     * @return A [NetworkResult] representing the result of the operation.
     */
    override suspend fun execute(operation: Operation): NetworkResult {
        return try {
            val url = endpointResolver.resolve(operation)
            val method = HttpMethodResolver.resolve(operation.type)

            //request
            val response = client.request(url) {
                this.method = method
                contentType(ContentType.Application.Json)

                header("X-Idempotency-Key", operation.idempotencyKey)

                if (operation.type != OperationType.DELETE) {
                    setBody(operation.payloadJson)
                }
            }

            //response
            if (response.status.isSuccess()) {
                NetworkResult.Success(
                    responsePayloadJson = response.bodyAsText(),
                    serverChangeToken = response.headers["X-Change-Token"]
                )
            } else {
                NetworkResult.Failure(
                    httpCode = response.status.value,
                    errorMessage = response.bodyAsText(),
                    retryable = ErrorClassifier.isRetryable(response)
                )
            }
        } catch (e: Exception) {
            NetworkResult.Failure(
                httpCode = null,
                errorMessage = e.message ?: "Unknown error",
                retryable = true
            )
        }
    }

    /**
     * Fetches changes from the server since the given [sinceToken].
     *
     * @param sinceToken The token to use for fetching changes.
     * @return A [FetchResult] representing the result of the fetch.
     */
    override suspend fun fetchChanges(sinceToken: String?): FetchResult {
        requireNotNull(deltaSyncEndpoint) {
            "deltaSyncEndpoint must be provided for fetchChanges()"
        }

        val response = client.get(deltaSyncEndpoint) {
            sinceToken?.let {
                parameter("since", it)
            }
        }

        return FetchResult(
            changeJson = response.bodyAsText(),
            newSinceToken = response.headers["X-Change-Token"]
        )
    }

    /**
     * Returns whether this network adapter supports idempotency.
     *
     * @return `true` if this network adapter supports idempotency, `false` otherwise.
     */
    override fun supportsIdempotency(): Boolean = true

}