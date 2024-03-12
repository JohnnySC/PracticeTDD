package com.github.johnnysc.practicetdd

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class CacheFirstRepositoryTest {

    private lateinit var cacheDataSource: FakeCacheDataSource
    private lateinit var cloudDataSource: FakeCloudDataSource
    private lateinit var handleError: FakeHandleError
    private lateinit var repository: ListRepository

    @Before
    fun setup() {
        cacheDataSource = FakeCacheDataSource()
        cloudDataSource = FakeCloudDataSource()
        handleError = FakeHandleError()
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

        val actual: List<Int> = repository.load()
        val expected: LoadResult = LoadResult.Success(data = listOf(1, 2, 3))
        assertEquals(expected, actual)
    }

    @Test
    fun cacheEmptyCloudSuccess() = runBlocking {
        cacheDataSource.empty()
        cloudDataSource.givesSuccess()

        val actual: List<Int> = repository.load()
        val expected: LoadResult = LoadResult.Success(data = listOf(4, 5))
        assertEquals(expected, actual)

        cacheDataSource.checkSaved(expected = listOf(4, 5))
    }

    @Test
    fun cacheEmptyCloudError() = runBlocking {
        cacheDataSource.empty()
        cloudDataSource.givesError()

        val actual: List<Int> = repository.load()
        val expected: LoadResult = LoadResult.Error(message = "no internet")
        assertEquals(expected, actual)

        handleError.check(UnknownHostException())
    }
}