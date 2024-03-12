package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals

class FakeHandleError  : HandleError {

    private var actualError : Exception = Exception()

    override fun handle(error: Exception) : String {
        actualError = error
        return "no internet"
    }

    fun check(expectedError: Exception) {
        assertEquals(expectedError, actualError)
    }
}