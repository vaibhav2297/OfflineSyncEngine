package io.theta.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.theta.ktor.endpoint.DefaultEndpointResolver

// create a client
val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

// create network adapter
val networkAdapter = KtorNetworkAdapter(
    client = client,
    endpointResolver = DefaultEndpointResolver("https://example.com/")
)

