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

    private class FakeNow : Now {

        var time: Long = 0

        override fun now(): Long {
            return time
        }
    }
}