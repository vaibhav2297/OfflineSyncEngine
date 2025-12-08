package io.theta.offlinesync.core

/**
 * Represents the result of fetching changes from the server.
 *
 * @property changeJson A JSON string containing the changes fetched from the server.
 * @property newSinceToken A token that represents the point in time of the last fetch.
 *                       This token can be used in subsequent fetches to get only the
 *                       changes that have occurred since the last sync.
 */
data class FetchResult(
    val changeJson: String,
    val newSinceToken: String?
)
