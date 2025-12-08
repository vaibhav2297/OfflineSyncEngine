package io.theta.offlinesync.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.theta.offlinesync.room.entity.QueueEntity

@Dao
interface QueueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: QueueEntity)

    @Query("""
        SELECT * FROM sync_queue
        WHERE status IN (:pendingStatuses)
        ORDER BY priority DESC, created_at ASC
        LIMIT :limit
    """)
    suspend fun peekPending(limit: Int, pendingStatuses: Array<String> = arrayOf("PENDING", "FAILED")): List<QueueEntity>

    @Query("SELECT COUNT(*) FROM sync_queue WHERE status IN (:pendingStatuses)")
    suspend fun countPending(pendingStatuses: Array<String> = arrayOf("PENDING", "FAILED")): Long

    @Query("SELECT * FROM sync_queue WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): QueueEntity?

    @Query("DELETE FROM sync_queue WHERE id = :id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun update(entity: QueueEntity)

    @Query("UPDATE sync_queue SET attempts = :attempts WHERE id = :id")
    suspend fun updateAttempts(id: String, attempts: Int)

    // mark success -> remove by default
    @Transaction
    suspend fun markSuccessAndRemove(id: String) {
        deleteById(id)
    }

    @Query("UPDATE sync_queue SET status = :status, last_error = :lastError WHERE id = :id")
    suspend fun markFailed(id: String, status: String, lastError: String?)
}