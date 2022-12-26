package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 26.12.2022
 */
class TimedCacheDataSourceTest {

    @Test
    fun test() {
        val now = FakeNow()
        val cacheDataSource: CacheDataSource =
            CacheDataSource.Timed(now = now, lifeTimeMillis = 1000)

        cacheDataSource.add(item = SimpleData("one"))

        now.time = 990

        val actual: List<SimpleData> = cacheDataSource.data()
        val expected = SimpleData("one")
        assertEquals(expected, actual[0])
        assertEquals(1, actual.size)

        now.time = 1010

        assertEquals(0, cacheDataSource.data().size)
    }

    @Test
    fun `test many items`() {
        val now = FakeNow()
        val cacheDataSource: CacheDataSource =
            CacheDataSource.Timed(now, lifeTimeMillis = 1000)

        cacheDataSource.add(SimpleData("one"))

        now.time = 300
        cacheDataSource.add(SimpleData("two"))

        now.time = 750
        cacheDataSource.add(SimpleData("three"))

        now.time = 999
        var expected = listOf(SimpleData("one"), SimpleData("two"), SimpleData("three"))
        var actual = cacheDataSource.data()
        assertEquals(expected, actual)

        now.time = 1001
        expected = listOf(SimpleData("two"), SimpleData("three"))
        actual = cacheDataSource.data()
        assertEquals(expected, actual)

        now.time = 1301
        expected = listOf(SimpleData("three"))
        actual = cacheDataSource.data()
        assertEquals(expected, actual)

        now.time = 1751
        expected = listOf()
        actual = cacheDataSource.data()
        assertEquals(expected, actual)
    }

    private class FakeNow : Now {

        var time: Long = 0

        override fun now(): Long {
            return time
        }
    }
}