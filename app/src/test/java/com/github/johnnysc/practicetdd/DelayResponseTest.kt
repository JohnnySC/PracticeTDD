package com.github.johnnysc.practicetdd

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

/**
 * @author Asatryan on 26.12.2022
 */
class DelayResponseTest {

    @Test
    fun test_delay() = runBlocking {
        val now: Now = Now.Base()
        val delayResponse: DelayResponse = DelayResponse.Base(now = now)
        val start: Long = now.time()
        val actual: String = delayResponse.delayAfter(delayInMillis = 1500L) {
            delay(500)
            "value"
        }
        Assert.assertEquals(true, now.time() - start in 1500..1600)
        Assert.assertEquals("value", actual)
    }

    @Test
    fun test_longer_delay() = runBlocking {
        val now = Now.Base()
        val delayResponse: DelayResponse = DelayResponse.Base(now)
        val start: Long = now.time()
        val actual: Int = delayResponse.delayAfter(delayInMillis = 1500L) {
            delay(2000)
            78
        }
        Assert.assertEquals(true, now.time() - start in 2000..2200)
        Assert.assertEquals(78, actual)
    }

    @Test
    fun test_no_delay() = runBlocking {
        val now = Now.Base()
        val delayResponse: DelayResponse = DelayResponse.Base(now)
        val start: Long = now.time()
        val actual: Char = delayResponse.delayAfter(delayInMillis = 0L) {
            delay(300)
            'x'
        }
        Assert.assertEquals(true, now.time() - start in 300..330)
        Assert.assertEquals('x', actual)
    }
}