package com.github.johnnysc.practicetdd

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class CloudFirstRepositoryTest {

    private lateinit var cacheDataSource: FakeCacheDataSource
    private lateinit var cloudDataSource: FakeCloudDataSource
    private lateinit var handleError: FakeHandleError
    private lateinit var repository: ListRepository

    @Before
    fun setup() {
        cacheDataSource = FakeCacheDataSource()
        cloudDataSource = FakeCloudDataSource()
        handleError = FakeHandleError()
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

        val actual: List<Int> = repository.load()
        val expected: LoadResult = LoadResult.Success(data = listOf(4, 5))
        Assert.assertEquals(expected, actual)

        cacheDataSource.checkSaved(expected = listOf(4, 5))
    }

    @Test
    fun cloudErrorCacheNotEmpty() = runBlocking {
        cloudDataSource.givesError()
        cacheDataSource.notEmpty()

        val actual: List<Int> = repository.load()
        val expected: LoadResult = LoadResult.Success(data = listOf(1, 2, 3))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun cloudErrorCacheEmpty() = runBlocking {
        cloudDataSource.givesError()
        cacheDataSource.empty()

        val actual: List<Int> = repository.load()
        val expected: LoadResult = LoadResult.Error(message = "no internet")
        Assert.assertEquals(expected, actual)

        handleError.check(UnknownHostException())
    }
}