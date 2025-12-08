package io.theta.offlinesync.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_queue")
data class QueueEntity(
    @PrimaryKey val id: String,

    @ColumnInfo(name = "resource")
    val resource: String,

    @ColumnInfo(name = "resource_id")
    val resourceId: String?,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "payload_json")
    val payloadJson: String,

    @ColumnInfo(name = "idempotency_key")
    val idempotencyKey: String,

    @ColumnInfo(name = "attempts")
    val attempts: Int = 0,

    @ColumnInfo(name = "status")
    val status: String = "PENDING",

    @ColumnInfo(name = "priority")
    val priority: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAtEpochMs: Long,

    @ColumnInfo(name = "last_error")
    val lastError: String? = null,

    @ColumnInfo(name = "schema_version")
    val schemaVersion: Int = 1
)
