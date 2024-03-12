package com.github.johnnysc.practicetdd

import java.net.UnknownHostException

class FakeCloudDataSource : CloudDataSource {

    private var success: Boolean = false
    private val list = listOf(4, 5)

    override suspend fun load(): List<Int> {
        if (success)
            return list
        else
            throw UnknownHostException()
    }

    fun givesSuccess() {
        success = true
    }

    fun givesError() {
        success = false
    }
}