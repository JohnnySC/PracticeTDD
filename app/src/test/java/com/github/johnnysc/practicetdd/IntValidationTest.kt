package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 26.12.2022
 */
class IntValidationTest {

    @Test
    fun test_positive_success() {
        val newIntPositiveValidation = NewInt.Positive()
        val newIntNegativeValidation = NewInt.Negative()
        val list = listOf(1, 2, 3, 4, 5, 6, 12, 13, 234, 1234, Int.MAX_VALUE)
        list.forEach { number ->
            assertEquals(true, newIntPositiveValidation.isValid(number = number))
            assertEquals(false, newIntNegativeValidation.isValid(number = number))
        }
    }

    @Test
    fun test_positive_not_success() {
        val newIntPositiveValidation = NewInt.Positive()
        val newIntNegativeValidation = NewInt.Negative()
        val list = listOf(-1, -2, -3, -4, -5, -6, -12, -13, -234, -1234, Int.MIN_VALUE)
        list.forEach { number ->
            assertEquals(false, newIntPositiveValidation.isValid(number = number))
            assertEquals(true, newIntNegativeValidation.isValid(number = number))
        }
    }

    @Test
    fun test_odd_success() {
        val newIntOddValidation = NewInt.Odd()
        val list = listOf(1, 3, 5, 7, 9, 13, 15, 11, 34589, -3, -7, -893)
        list.forEach { number ->
            assertEquals(true, newIntOddValidation.isValid(number = number))
        }
    }

    @Test
    fun test_odd_not_success() {
        val newIntOddValidation = NewInt.Odd()
        val list = listOf(0, 2, 4, 6, 8, 10, -2, -6, -456, 898)
        list.forEach { number ->
            assertEquals(false, newIntOddValidation.isValid(number = number))
        }
    }

    @Test
    fun test_odd_and_positive_success() {
        val validation = NewInt.Odd(NewInt.Positive())
        val list = listOf(1, 3, 5, 7, 9, 13, 15, 11, 34589)
        list.forEach { number ->
            assertEquals(true, validation.isValid(number = number))
        }
    }

    @Test
    fun test_odd_and_positive_not_success() {
        val validation = NewInt.Odd(NewInt.Positive())
        val list = listOf(2, -3, -4, -7, -9, -13, -15, -11, -34589, 456, 878, 100034)
        list.forEach { number ->
            assertEquals(false, validation.isValid(number = number))
        }
    }

    @Test
    fun test_less_than_success() {
        val validation = NewInt.Less(limit = 10)
        val list = listOf(9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -10, -100, -138965, Int.MIN_VALUE)
        list.forEach { number ->
            assertEquals(true, validation.isValid(number = number))
        }
    }

    @Test
    fun test_less_than_not_success() {
        val validation = NewInt.Less(limit = 10)
        val list = listOf(10, 11, 12, 13, 1487, 234234, 1234235243, Int.MAX_VALUE)
        list.forEach { number ->
            assertEquals(false, validation.isValid(number = number))
        }
    }

    @Test
    fun test_triple_validation_success() {
        val validation = NewInt.Positive(NewInt.Odd(NewInt.Less(10)))
        val list = listOf(1, 3, 5, 7, 9)
        list.forEach { number ->
            assertEquals(true, validation.isValid(number = number))
        }
    }

    @Test
    fun test_triple_validation_not_success() {
        val validation = NewInt.Positive(NewInt.Odd(NewInt.Less(10)))
        val list = listOf(-3, 4, 10, Int.MAX_VALUE, 7654, 765, -98989, -9898)
        list.forEach { number ->
            assertEquals(false, validation.isValid(number = number))
        }
    }
}