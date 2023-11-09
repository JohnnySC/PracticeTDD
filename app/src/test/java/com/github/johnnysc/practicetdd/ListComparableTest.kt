package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

class ListComparableTest {

    @Test
    fun test_areItemsTheSame_false() {
        val first: ListComparable = ListComparable.Base(id = "1", name = "a")
        val second: ListComparable = ListComparable.Base(id = "2", name = "a")

        assertEquals(false, first.areItemsTheSame(other = second))
    }

    @Test
    fun test_areItemsTheSame_true() {
        val first: ListComparable = ListComparable.Base(id = "1", name = "a")
        val second: ListComparable = ListComparable.Base(id = "1", name = "b")

        assertEquals(true, first.areItemsTheSame(other = second))
    }

    @Test
    fun test_areContentsTheSame_false() {
        val first: ListComparable = ListComparable.Base(id = "1", name = "a")
        val second: ListComparable = ListComparable.Base(id = "1", name = "b")

        assertEquals(false, first.areContentsTheSame(other = second))
    }

    @Test
    fun test_areContentsTheSame_true() {
        val first: ListComparable = ListComparable.Base(id = "1", name = "a")
        val second: ListComparable = ListComparable.Base(id = "1", name = "a")

        assertEquals(true, first.areContentsTheSame(other = second))
    }
}
