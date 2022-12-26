package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.math.BigInteger

/**
 * @author Asatryan on 26.12.2022
 */
class FactorialTest {

    @Test
    fun test_integers() {
        val sourceList = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        val expectedList = listOf(1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600)

        val factorial = Factorial.Int()
        sourceList.forEachIndexed { index, item ->
            assertEquals(expectedList[index], factorial.value(item))
        }
    }

    @Test
    fun test_doubles() {
        val factorial = Factorial.Double()
        val actual = factorial.value(13.0)
        val expected = 6.227020800E09
        assertEquals(expected, actual, 0.000000000001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun test_negative() {
        val int = TestInt()
        val double = TestDouble()
        val bigInteger = TestBigInteger()
        val factory = Factorial.Factory(int, double, bigInteger)
        factory.value(-1)
    }

    @Test
    fun test_zero() {
        val factory = Factorial.Factory(Factorial.Int(), Factorial.Double(), Factorial.BigInteger())
        val actual = factory.value(0)
        val expected = 1
        assertEquals(expected, actual)
    }

    @Test
    fun test_int() {
        val int = TestInt()
        val double = TestDouble()
        val bigInteger = TestBigInteger()
        val factory = Factorial.Factory(int, double, bigInteger)
        factory.value(5)
        assertEquals(5, int.value)
        assertEquals(0.0.toString(), double.value.toString())
        assertEquals(java.math.BigInteger.ZERO, bigInteger.value)
    }

    @Test
    fun test_double() {
        val int = TestInt()
        val double = TestDouble()
        val bigInteger = TestBigInteger()
        val factory = Factorial.Factory(int, double, bigInteger)
        factory.value(15)
        assertEquals(0, int.value)
        assertEquals(15.0.toString(), double.value.toString())
        assertEquals(java.math.BigInteger.ZERO, bigInteger.value)
    }

    @Test
    fun test_big_integer() {
        val int = TestInt()
        val double = TestDouble()
        val bigInteger = TestBigInteger()
        val factory = Factorial.Factory(int, double, bigInteger)
        factory.value(1500)
        assertEquals(0, int.value)
        assertEquals(0.0.toString(), double.value.toString())
        assertEquals(java.math.BigInteger.valueOf(1500), bigInteger.value)
    }

    @Test(expected = IllegalStateException::class)
    fun test_too_big_number() {
        val int = TestInt()
        val double = TestDouble()
        val bigInteger = TestBigInteger()
        val factory = Factorial.Factory(int, double, bigInteger)
        factory.value(12000)
    }

    private class TestInt : Factorial<kotlin.Int> {
        var value: Int = 0

        override fun value(number: Int): Int {
            value = number
            return value
        }
    }

    private class TestDouble : Factorial<kotlin.Double> {

        var value: Double = 0.0

        override fun value(number: Double): Double {
            value = number
            return value
        }
    }

    private class TestBigInteger : Factorial<java.math.BigInteger> {

        var value = java.math.BigInteger.ZERO

        override fun value(number: BigInteger): BigInteger {
            value = number
            return value
        }
    }
}