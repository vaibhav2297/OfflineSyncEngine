package io.theta.offlinesync.core

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class InMemoryQueueStore : QueueStore {

    private val items = mutableListOf<Operation>()

    override suspend fun enqueue(operation: Operation) {
        items.add(operation)
    }

    override suspend fun peek(limit: Int): List<Operation> = items.take(limit)

    override suspend fun markSuccess(operationId: String) {
        items.removeIf { it.id == operationId }
    }

    override suspend fun markFailed(
        operationId: String,
        error: String,
        retryable: Boolean
    ) { }

    override suspend fun updateAttempts(opId: String, attempts: Int) {
        items.find { it.id == opId }?.let {
            val index = items.indexOf(it)
            items[index] = it.copy(attempts = attempts)
        }
    }

    override suspend fun getPendingCount(): Long = items.size.toLong()

    override suspend fun getById(operationId: String): Operation? = items.find { it.id == operationId }

    override suspend fun remove(operationId: String) {
        items.removeAll { it.id == operationId }
    }
}

class FakeNetworkAdapter : NetworkAdapter {

    override suspend fun execute(operation: Operation): NetworkResult {
        return NetworkResult.Success("{\"ok\":true}")
    }

    override suspend fun fetchChanges(sinceToken: String?): FetchResult {
        return FetchResult("[]", null)
    }

    override fun supportsIdempotency() = true
}

class SyncEngineTest {

    @Test
    fun `enqueue and runNow should process operation`() = runBlocking {
        val store = InMemoryQueueStore()
        val adapter = FakeNetworkAdapter()

        val engine = SyncEngine
            .Builder(
                queueStore = store,
                networkAdapter = adapter
            )
            .build()


        val op = Operation(
            id = "1",
            resource = "notes",
            resourceId = "1",
            type = OperationType.CREATE,
            payloadJson = "{\"text\":\"hello\"}",
            idempotencyKey = "k1",
            createdAtEpochMs = System.currentTimeMillis()
        )

        engine.enqueue(op)
        engine.runNow()

        val count = store.getPendingCount()
        assertEquals(0L, count)
    }
}