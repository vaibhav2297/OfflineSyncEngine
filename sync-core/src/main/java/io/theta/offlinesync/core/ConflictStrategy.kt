package io.theta.offlinesync.core

/**
 * Defines a strategy for resolving conflicts between local and server data.
 * Applications can provide their own implementations of this interface to handle
 * data conflicts in a customized manner.
 */
interface ConflictStrategy {

    /**
     * Resolves a conflict between a local and a server version of a resource.
     *
     * @param localJson A JSON string representing the local version of the data.
     * @param serverJson A JSON string representing the server version of the data.
     * @return A JSON string representing the resolved data, which will be used
     *         to update the local state. The implementation can choose to return
     *         the `localJson`, the `serverJson`, or a merged version of both.
     */
    suspend fun resolve(
        localJson: String,
        serverJson: String
    ): String
}
