package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals

class FakeCacheDataSource(private val order: Order) : CacheDataSource {

    private var actualList = listOf<Int>()

    override suspend fun load(): List<Int> {
        order.add(CACHE_LOAD)
        return actualList
    }

    override suspend fun save(data: List<Int>) {
        order.add(CACHE_SAVE)
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

    companion object {
        const val CACHE_LOAD = "CacheDataSource#load"
        const val CACHE_SAVE = "CacheDataSource#save"
    }
}