package com.github.johnnysc.practicetdd
import org.junit.Assert.*
import org.junit.Test

/**
 * @author Asatryan on 21.02.2023
 */
class NumbersTest {

    @Test
    fun `test min`() {
        val numbers: Numbers = Numbers.Base(first = 5, second = 10)
        val actual: Int = numbers.minimum()
        val expected = 5
        assertEquals(expected, actual)
    }

    @Test
    fun `test maximum`() {
        val numbers: Numbers = Numbers.Base(first = -100, second = 65)
        val actual: Int = numbers.maximum()
        val expected = 65
        assertEquals(expected, actual)
    }

    @Test
    fun `test list`() {
        val numbers: Numbers = Numbers.List(list = listOf(5, 3, 1, -10, 98))
        val minimum: Int = numbers.minimum()
        val maximum: Int = numbers.maximum()
        assertEquals(-10, minimum)
        assertEquals(98, maximum)
    }

    @Test
    fun `test empty list`() {
        val numbers: Numbers = Numbers.List(list = listOf())
        val minimum: Int = numbers.minimum()
        val maximum: Int = numbers.maximum()
        assertEquals(Int.MIN_VALUE, minimum)
        assertEquals(Int.MAX_VALUE, maximum)
    }
}