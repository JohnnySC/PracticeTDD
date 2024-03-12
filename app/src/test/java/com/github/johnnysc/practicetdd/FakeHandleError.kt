package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals

class FakeHandleError(private val order: Order) : HandleError {

    private var actualError: Exception = Exception()

    override fun handle(error: Exception): String {
        order.add(HANDLE)
        actualError = error
        return "no internet"
    }

    fun check(expectedError: Exception) {
        assertEquals(expectedError::class, actualError::class)
    }
    companion object {
        const val HANDLE = "HandleError#handle"
    }
}