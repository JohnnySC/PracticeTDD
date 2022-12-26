package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 26.12.2022
 */
class RangeTest {

    @Test
    fun test_range_2_defined() {
        val range: RangeLimits = RangeLimits.Base(list = listOf<Int>(-5, -3, 0, 1, 3, 5))
        val value: Int = 2
        var actual: RangePair = range.pair(number = value)
        var expected = RangePair(left = 1, right = 3)
        assertEquals(expected, actual)
        actual = range.pair(number = 4)
        expected = RangePair(left = 3, right = 5)
        assertEquals(expected, actual)
    }

    @Test
    fun test_empty_list() {
        val range: RangeLimits = RangeLimits.Base(list = emptyList<Int>())
        val actual: RangePair = range.pair(number = 2)
        val expected = RangePair(left = Int.MIN_VALUE, right = Int.MAX_VALUE)
        assertEquals(expected, actual)
    }

    @Test
    fun test_one_item() {
        val range: RangeLimits = RangeLimits.Base(list = listOf<Int>(5))
        val actual: RangePair = range.pair(number = 2)
        val expected = RangePair(left = Int.MIN_VALUE, right = 5)
        assertEquals(expected, actual)
    }

    @Test
    fun test_one_item_left() {
        val range: RangeLimits = RangeLimits.Base(list = listOf<Int>(0))
        val actual: RangePair = range.pair(number = 2)
        val expected = RangePair(left = 0, right = Int.MAX_VALUE)
        assertEquals(expected, actual)
    }

    @Test
    fun test_one_item_same() {
        val range: RangeLimits = RangeLimits.Base(list = listOf<Int>(2))
        val actual: RangePair = range.pair(number = 2)
        val expected = RangePair(left = Int.MIN_VALUE, right = Int.MAX_VALUE)
        assertEquals(expected, actual)
    }

    @Test
    fun test_two_items_left() {
        val range: RangeLimits = RangeLimits.Base(list = listOf<Int>(1, 3))
        val actual: RangePair = range.pair(number = 0)
        val expected = RangePair(left = Int.MIN_VALUE, right = 1)
        assertEquals(expected, actual)
    }

    @Test
    fun test_two_items_right() {
        val range: RangeLimits = RangeLimits.Base(list = listOf<Int>(1, 3))
        val actual: RangePair = range.pair(number = 5)
        val expected = RangePair(left = 3, right = Int.MAX_VALUE)
        assertEquals(expected, actual)
    }

    @Test
    fun test_two_item_right_same() {
        val range: RangeLimits = RangeLimits.Base(list = listOf<Int>(1, 3))
        val actual: RangePair = range.pair(number = 3)
        val expected = RangePair(left = 1, right = Int.MAX_VALUE)
        assertEquals(expected, actual)
    }

    @Test
    fun test_two_item_left_same() {
        val range: RangeLimits = RangeLimits.Base(list = listOf<Int>(1, 3))
        val actual: RangePair = range.pair(number = 1)
        val expected = RangePair(left = Int.MIN_VALUE, right = 3)
        assertEquals(expected, actual)
    }

    @Test
    fun test_min_value() {
        val range: RangeLimits = RangeLimits.Base(list = listOf<Int>(1, 3, 5))
        val actual: RangePair = range.pair(number = Int.MIN_VALUE)
        val expected = RangePair(left = Int.MIN_VALUE, right = 1)
        assertEquals(expected, actual)
    }

    @Test
    fun test_max_value() {
        val range: RangeLimits = RangeLimits.Base(list = listOf<Int>(1, 3, 5))
        val actual: RangePair = range.pair(number = Int.MAX_VALUE)
        val expected = RangePair(left = 5, right = Int.MAX_VALUE)
        assertEquals(expected, actual)
    }
}