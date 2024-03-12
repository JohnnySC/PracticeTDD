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

class CacheFirstRepositoryTest {

    private lateinit var cacheDataSource: FakeCacheDataSource
    private lateinit var cloudDataSource: FakeCloudDataSource
    private lateinit var handleError: FakeHandleError
    private lateinit var order: Order
    private lateinit var repository: ListRepository

    @Before
    fun setup() {
        order = Order(mutableListOf())
        cacheDataSource = FakeCacheDataSource(order = order)
        cloudDataSource = FakeCloudDataSource(order = order)
        handleError = FakeHandleError(order = order)
        repository = ListRepository.CacheFirst(
            cacheDataSource = cacheDataSource,
            cloudDataSource = cloudDataSource,
            handleError = handleError
        )
    }

    @Test
    fun cacheNotEmptyCloudError() = runBlocking {
        cloudDataSource.givesError()
        cacheDataSource.notEmpty()

        val actual: LoadResult = repository.load()
        val expected: LoadResult = LoadResult.Success(data = listOf(1, 2, 3))
        assertEquals(expected, actual)

        assertEquals(Order(mutableListOf(CACHE_LOAD)), order)
    }

    @Test
    fun cacheEmptyCloudSuccess() = runBlocking {
        cacheDataSource.empty()
        cloudDataSource.givesSuccess()

        val actual: LoadResult = repository.load()
        val expected: LoadResult = LoadResult.Success(data = listOf(4, 5))
        assertEquals(expected, actual)

        cacheDataSource.checkSaved(expected = listOf(4, 5))

        assertEquals(Order(mutableListOf(CACHE_LOAD, CLOUD, CACHE_SAVE)), order)
    }

    @Test
    fun cacheEmptyCloudError() = runBlocking {
        cacheDataSource.empty()
        cloudDataSource.givesError()

        val actual: LoadResult = repository.load()
        val expected: LoadResult = LoadResult.Error(message = "no internet")
        assertEquals(expected, actual)

        handleError.check(UnknownHostException())

        assertEquals(Order(mutableListOf(CACHE_LOAD, CLOUD, HANDLE)), order)
    }
}