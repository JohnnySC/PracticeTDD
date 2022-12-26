package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 26.12.2022
 */
class ContainsStringTest {

    @Test
    fun test() {
        val forWrapper = TestForWrapper()
        val contains: Contains = Contains.Base(forWrapper)
        val actual =
            contains.contains(collection = listOf("a", "b", "c", "d", "e", "f"), item = "c")
        val expected = true
        assertEquals(expected, actual)
        assertEquals(3, forWrapper.count)
    }

    @Test
    fun test_not() {
        val forWrapper = TestForWrapper()
        val contains: Contains = Contains.Base(forWrapper)
        val actual =
            contains.contains(collection = listOf("a", "b", "c", "d", "e", "f"), item = "x")
        val expected = false
        assertEquals(expected, actual)
        assertEquals(6, forWrapper.count)
    }

    class TestForWrapper(private val forWrapper: For = For.Base()) : For {

        var count = 0

        override fun repeat(max: Int, start: Int, block: (Int) -> Boolean) {
            count = 0
            forWrapper.repeat(max, start) {
                count++
                block.invoke(it)
            }
        }
    }
}