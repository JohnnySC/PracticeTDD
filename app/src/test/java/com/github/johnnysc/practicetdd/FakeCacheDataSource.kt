package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals

class FakeCacheDataSource : CacheDataSource {

    private var actualList = listOf<Int>()

    override suspend fun load(): List<Int> {
        return actualList
    }

    override suspend fun save(data: List<Int>) {
        actualList = data
    }

    fun notEmpty() {
        actualList = listOf(1, 2, 3)
    }

    fun empty() {
        actualList = emptyList()
    }

    fun checkSaved(expected: List<Int>) {
        assertEquals(expected, actualList)
    }
}