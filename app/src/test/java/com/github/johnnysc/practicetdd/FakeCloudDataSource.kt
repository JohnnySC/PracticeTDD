package com.github.johnnysc.practicetdd

import java.net.UnknownHostException

class FakeCloudDataSource(private val order: Order) : CloudDataSource {

    private var success: Boolean = false
    private val list = listOf(4, 5)

    override suspend fun load(): List<Int> {
        order.add(CLOUD)
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

    companion object {
        const val CLOUD = "CloudDataSource#load"
    }
}