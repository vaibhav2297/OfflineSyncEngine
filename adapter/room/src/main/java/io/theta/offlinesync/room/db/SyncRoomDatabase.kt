package io.theta.offlinesync.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.theta.offlinesync.room.dao.QueueDao
import io.theta.offlinesync.room.entity.QueueEntity

@Database(entities = [QueueEntity::class], version = 1, exportSchema = true)
abstract class SyncRoomDatabase : RoomDatabase() {

    abstract fun queueDao(): QueueDao
}