package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 26.12.2022
 */
class CounterTest {

    @Test
    fun test() {
        val callback: FakeCallback = FakeCallback.Base()
        val count: Count = Count.Base(callback)
        count.click()
        assertEquals("1", callback.count())
        count.click()
        assertEquals("2", callback.count())
        count.click()
        assertEquals("3", callback.count())
    }

    private interface FakeCallback : Count.Callback {

        fun count(): String

        class Base : FakeCallback {
            private var value = ""

            override fun provide(value: String) {
                this.value = value
            }

            override fun count(): String {
                return value
            }
        }
    }
}