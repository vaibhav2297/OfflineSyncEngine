package io.theta.offlinesync.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.theta.offlinesync.core.Operation
import io.theta.offlinesync.core.OperationType
import io.theta.offlinesync.room.db.SyncRoomDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class RoomQueueStoreInstrumentedTest {

    private lateinit var db: SyncRoomDatabase
    private lateinit var store: RoomQueueStore

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<android.content.Context>()

        db = Room.inMemoryDatabaseBuilder(
            context = ctx,
            klass = SyncRoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        store = RoomQueueStore(db.queueDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun enqueueAndPeekAndMarkSuccess() = runBlocking {

        val op = Operation(
            id = UUID.randomUUID().toString(),
            resource = "notes",
            resourceId = "1",
            type = OperationType.CREATE,
            payloadJson = "{\"text\":\"hello\"}",
            idempotencyKey = "k1",
            createdAtEpochMs = System.currentTimeMillis()
        )

        store.enqueue(op)
        val pending = store.peek(10)
        assertEquals(1, pending.size)
        assertEquals(op.id, pending[0].id)

        store.markSuccess(op.id)
        val count = store.getPendingCount()
        assertEquals(0L, count)
    }
}