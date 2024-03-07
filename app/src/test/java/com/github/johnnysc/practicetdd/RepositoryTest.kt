package com.github.johnnysc.practicetdd

import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryTest {

    @Test(timeout = 1100)
    fun multithreading() = runBlocking {
        val service: SomeService = FakeService()
        val repository: Repository = Repository.Base(service = service, dispatcher = Unconfined)
        val actual = repository.jokes()
        val expected = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        assertEquals(expected, actual)
    }
}

private class FakeService : SomeService {

    private var count = 0

    override suspend fun load(): String {
        delay(1000)
        return (count++).toString()
    }
}