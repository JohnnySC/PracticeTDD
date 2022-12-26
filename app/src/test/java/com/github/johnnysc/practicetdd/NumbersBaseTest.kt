package com.github.johnnysc.practicetdd

import org.junit.Assert
import org.junit.Test

/**
 * @author Asatryan on 26.12.2022
 */
class NumbersBaseTest {

    @Test
    fun test_sum_int_different_signs() {
        val numbers: Numbers = Numbers.Base(number1 = 200, number2 = -300)
        val isSumLong = numbers.isSumLong()
        val expected = false
        Assert.assertEquals(expected, isSumLong)
        val actual: Int = numbers.sumInt()
        val expectedNumber = -100
        Assert.assertEquals(expectedNumber, actual)
    }

    @Test
    fun test_sum_long_max() {
        val numbers: Numbers = Numbers.Base(2_000_000_000, 147483648)
        val isSumLong = numbers.isSumLong()
        val expected = true
        Assert.assertEquals(expected, isSumLong)
        val actual: Long = numbers.sumLong()
        val expectedNumber: Long = Int.MAX_VALUE + 1L
        Assert.assertEquals(expectedNumber, actual)
    }

    @Test
    fun test_sum_int_max() {
        val numbers: Numbers = Numbers.Base(2_000_000_000, 147483647)
        val isSumLong = numbers.isSumLong()
        val expected = false
        Assert.assertEquals(expected, isSumLong)
        val actual: Int = numbers.sumInt()
        val expectedNumber: Int = Int.MAX_VALUE
        Assert.assertEquals(expectedNumber, actual)
    }

    @Test
    fun test_sum_long_min() {
        val numbers: Numbers = Numbers.Base(-2_000_000_000, -147483649)
        val isSumLong = numbers.isSumLong()
        val expected = true
        Assert.assertEquals(expected, isSumLong)
        val actual: Long = numbers.sumLong()
        val expectedNumber: Long = Int.MIN_VALUE - 1L
        Assert.assertEquals(expectedNumber, actual)
    }

    @Test
    fun test_sum_int_min() {
        val numbers: Numbers = Numbers.Base(-2_000_000_000, -147483648)
        val isSumLong = numbers.isSumLong()
        val expected = false
        Assert.assertEquals(expected, isSumLong)
        val actual: Int = numbers.sumInt()
        val expectedNumber: Int = Int.MIN_VALUE
        Assert.assertEquals(expectedNumber, actual)
    }

    @Test(expected = IllegalAccessException::class)
    fun test_sum_int_no_check() {
        val numbers: Numbers = Numbers.Base(2_000_000_000, 2_000_000_000)
        numbers.sumInt()
    }

    @Test(expected = IllegalAccessException::class)
    fun test_sum_long_no_check() {
        val numbers: Numbers = Numbers.Base(2_000_000_000, 2_000_000_000)
        numbers.sumLong()
    }

    @Test(expected = IllegalStateException::class)
    fun test_sum_long_called_for_long_sum() {
        val numbers: Numbers = Numbers.Base(2_000_000_000, 2_000_000_000)
        val actual = numbers.isSumLong()
        val expected = true
        Assert.assertEquals(expected, actual)
        numbers.sumInt()
    }

    @Test(expected = IllegalStateException::class)
    fun test_sum_long_called_for_int_sum() {
        val numbers: Numbers = Numbers.Base(2_000_000_000, 1_000_000)
        val actual = numbers.isSumLong()
        val expected = false
        Assert.assertEquals(expected, actual)
        numbers.sumLong()
    }

    @Test
    fun test_difference() {
        val numbers: Numbers = Numbers.Base(12, 4)
        val actual = numbers.difference()
        val expected = 8
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun test_divide() {
        val numbers: Numbers = Numbers.Base(12, 4)
        val actual = numbers.divide()
        val expected = 3.0
        Assert.assertEquals(expected, actual, 0.000000000000001)
    }

    @Test
    fun test_divide_double() {
        val numbers: Numbers = Numbers.Base(10, 4)
        val actual = numbers.divide()
        val expected = 2.5
        Assert.assertEquals(expected, actual, 0.000000000000001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun test_divide_zero() {
        val numbers: Numbers = Numbers.Base(3, 0)
        numbers.divide()
    }
}