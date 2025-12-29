package io.theta.offlinesync.delta

/**
 * Stores and retrieves the token for the last successful delta sync.
 * Implementations can use SharedPreferences, DataStore, a database, etc.
 */
interface ChangeTokenStore {

    /**
     * Retrieves the last saved sync token.
     *
     * @return The last sync token, or `null` if no token has been saved.
     */
    suspend fun getLastToken(): String?

    /**
     * Saves the given sync token.
     *
     * @param token The sync token to save.
     */
    suspend fun saveToken(token: String)
}
