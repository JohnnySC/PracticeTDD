package com.github.johnnysc.practicetdd

import com.github.johnnysc.practicetdd.FakeCacheDataSource.Companion.CACHE_LOAD
import com.github.johnnysc.practicetdd.FakeCacheDataSource.Companion.CACHE_SAVE
import com.github.johnnysc.practicetdd.FakeCloudDataSource.Companion.CLOUD
import com.github.johnnysc.practicetdd.FakeHandleError.Companion.HANDLE
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class CloudFirstRepositoryTest {

    private lateinit var cacheDataSource: FakeCacheDataSource
    private lateinit var cloudDataSource: FakeCloudDataSource
    private lateinit var handleError: FakeHandleError
    private lateinit var repository: ListRepository
    private lateinit var order: Order

    @Before
    fun setup() {
        order = Order(mutableListOf())
        cacheDataSource = FakeCacheDataSource(order = order)
        cloudDataSource = FakeCloudDataSource(order = order)
        handleError = FakeHandleError(order = order)
        repository = ListRepository.CloudFirst(
            cacheDataSource = cacheDataSource,
            cloudDataSource = cloudDataSource,
            handleError = handleError
        )
    }

    @Test
    fun cloudSuccess() = runBlocking {
        cloudDataSource.givesSuccess()
        cacheDataSource.empty()

        val actual: LoadResult = repository.load()
        val expected: LoadResult = LoadResult.Success(data = listOf(4, 5))
        assertEquals(expected, actual)

        cacheDataSource.checkSaved(expected = listOf(4, 5))

        assertEquals(Order(mutableListOf(CLOUD, CACHE_SAVE)), order)
    }

    @Test
    fun cloudErrorCacheNotEmpty() = runBlocking {
        cloudDataSource.givesError()
        cacheDataSource.notEmpty()

        val actual: LoadResult = repository.load()
        val expected: LoadResult = LoadResult.Success(data = listOf(1, 2, 3))
        assertEquals(expected, actual)

        assertEquals(Order(mutableListOf(CLOUD, CACHE_LOAD)), order)
    }

    @Test
    fun cloudErrorCacheEmpty() = runBlocking {
        cloudDataSource.givesError()
        cacheDataSource.empty()

        val actual: LoadResult = repository.load()
        val expected: LoadResult = LoadResult.Error(message = "no internet")
        assertEquals(expected, actual)

        handleError.check(UnknownHostException())

        assertEquals(Order(mutableListOf(CLOUD, CACHE_LOAD, HANDLE)), order)
    }
}