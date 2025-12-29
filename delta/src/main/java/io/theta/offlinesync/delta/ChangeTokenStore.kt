package io.theta.offlinesync.delta

/**
 * Stores and retrieves last successful delta sync token.
 * Implementations: SharedPreferences, DataStore, DB, etc.
 */
interface ChangeTokenStore {

    suspend fun getLastToken(): String?

    suspend fun saveToken(token: String)
}