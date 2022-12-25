package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 25.12.2022
 */
class StackTest {

    @Test(expected = IllegalStateException::class)
    fun `test invalid object negative count`() {
        MyStack.Base<CustomObject>(maxCount = -1)
    }

    @Test(expected = IllegalStateException::class)
    fun `test invalid object zero count`() {
        MyStack.Base<CustomObject>(maxCount = 0)
    }

    @Test(expected = IllegalStateException::class)
    fun `test pop item from empty stack`() {
        val stack = MyStack.Base<CustomObject>(maxCount = 1)
        stack.pop()
    }

    @Test
    fun `test push more items than max count`() {
        val stack = MyStack.Base<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        try {
            stack.push(item = CustomObject("2"))
        } catch (e: Exception) {
            assertEquals(true, e is IllegalStateException)
            assertEquals("Stack overflow exception, maximum is 1", e.message)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `test pop more than pushed`() {
        val stack = MyStack.Base<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        stack.pop()
        stack.pop()
    }

    @Test
    fun `test pop`() {
        val stack = MyStack.Base<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        val actual = stack.pop()
        val expected = CustomObject("1")
        assertEquals(expected, actual)
    }

    @Test
    fun `test pop twice`() {
        val stack = MyStack.Base<CustomObject>(maxCount = 2)
        stack.push(item = CustomObject("1"))
        stack.push(item = CustomObject("2"))
        var actual = stack.pop()
        var expected = CustomObject("2")
        assertEquals(expected, actual)
        actual = stack.pop()
        expected = CustomObject("1")
        assertEquals(expected, actual)
    }

    @Test
    fun `test push and pop twice`() {
        val stack = MyStack.Base<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        var actual = stack.pop()
        var expected = CustomObject("1")
        assertEquals(expected, actual)
        stack.push(item = CustomObject("2"))
        actual = stack.pop()
        expected = CustomObject("2")
        assertEquals(expected, actual)
    }
}

private data class CustomObject(val id: String)