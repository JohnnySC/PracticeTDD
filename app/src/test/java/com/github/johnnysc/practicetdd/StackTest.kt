package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test


class StackTest {

    @Test(expected = IllegalStateException::class)
    fun `test invalid object negative count`() {
        MyStack.LIFO<CustomObject>(maxCount = -1)
    }

    @Test(expected = IllegalStateException::class)
    fun `test invalid object negative count fifo`() {
        MyStack.FIFO<CustomObject>(maxCount = -1)
    }

    @Test(expected = IllegalStateException::class)
    fun `test invalid object zero count`() {
        MyStack.LIFO<CustomObject>(maxCount = 0)
    }

    @Test(expected = IllegalStateException::class)
    fun `test invalid object zero count fifo`() {
        MyStack.FIFO<CustomObject>(maxCount = 0)
    }

    @Test(expected = IllegalStateException::class)
    fun `test pop item from empty stack`() {
        val stack = MyStack.LIFO<CustomObject>(maxCount = 1)
        stack.pop()
    }

    @Test(expected = IllegalStateException::class)
    fun `test pop item from empty stack fifo`() {
        val stack = MyStack.FIFO<CustomObject>(maxCount = 1)
        stack.pop()
    }

    @Test
    fun `test push more items than max count`() {
        val stack = MyStack.LIFO<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        try {
            stack.push(item = CustomObject("2"))
        } catch (e: Exception) {
            assertEquals(IllegalStateException::class.java, e.javaClass)
            assertEquals("Stack overflow exception, maximum is 1", e.message)
        }
    }

    @Test
    fun `test push more items than max count fifo`() {
        val stack = MyStack.FIFO<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        try {
            stack.push(item = CustomObject("2"))
        } catch (e: Exception) {
            assertEquals(IllegalStateException::class.java, e.javaClass)
            assertEquals("Stack overflow exception, maximum is 1", e.message)
        }
    }

    @Test
    fun `test push more items than max count 2`() {
        val stack = MyStack.LIFO<CustomObject>(maxCount = 2)
        stack.push(item = CustomObject("1"))
        stack.push(item = CustomObject("2"))
        try {
            stack.push(item = CustomObject("3"))
        } catch (e: Exception) {
            assertEquals(IllegalStateException::class.java, e.javaClass)
            assertEquals("Stack overflow exception, maximum is 2", e.message)
        }
    }

    @Test
    fun `test push more items than max count fifo 2`() {
        val stack = MyStack.FIFO<CustomObject>(maxCount = 2)
        stack.push(item = CustomObject("1"))
        stack.push(item = CustomObject("2"))
        try {
            stack.push(item = CustomObject("3"))
        } catch (e: Exception) {
            assertEquals(IllegalStateException::class.java, e.javaClass)
            assertEquals("Stack overflow exception, maximum is 2", e.message)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `test pop more than pushed`() {
        val stack = MyStack.LIFO<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        stack.pop()
        stack.pop()
    }

    @Test(expected = IllegalStateException::class)
    fun `test pop more than pushed fifo`() {
        val stack = MyStack.FIFO<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        stack.pop()
        stack.pop()
    }

    @Test
    fun `test pop`() {
        val stack = MyStack.LIFO<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        val actual = stack.pop()
        val expected = CustomObject("1")
        assertEquals(expected, actual)
    }

    @Test
    fun `test pop fifo`() {
        val stack = MyStack.FIFO<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        val actual = stack.pop()
        val expected = CustomObject("1")
        assertEquals(expected, actual)
    }

    @Test
    fun `test pop twice`() {
        val stack = MyStack.LIFO<CustomObject>(maxCount = 2)
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
    fun `test pop twice fifo`() {
        val stack = MyStack.FIFO<CustomObject>(maxCount = 2)
        stack.push(item = CustomObject("1"))
        stack.push(item = CustomObject("2"))
        var actual = stack.pop()
        var expected = CustomObject("1")
        assertEquals(expected, actual)
        actual = stack.pop()
        expected = CustomObject("2")
        assertEquals(expected, actual)
    }

    @Test
    fun `test push and pop twice`() {
        val stack = MyStack.LIFO<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        var actual = stack.pop()
        var expected = CustomObject("1")
        assertEquals(expected, actual)
        stack.push(item = CustomObject("2"))
        actual = stack.pop()
        expected = CustomObject("2")
        assertEquals(expected, actual)
    }

    @Test
    fun `test push and pop twice fifo`() {
        val stack = MyStack.FIFO<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        var actual = stack.pop()
        var expected = CustomObject("1")
        assertEquals(expected, actual)
        stack.push(item = CustomObject("2"))
        actual = stack.pop()
        expected = CustomObject("2")
        assertEquals(expected, actual)
    }


    @Test
    fun `test push and pop third fifo`() {
        val stack = MyStack.FIFO<CustomObject>(maxCount = 1)
        stack.push(item = CustomObject("1"))
        var actual = stack.pop()
        var expected = CustomObject("1")
        assertEquals(expected, actual)
        stack.push(item = CustomObject("2"))
        actual = stack.pop()
        expected = CustomObject("2")
        assertEquals(expected, actual)

        stack.push(item = CustomObject("3"))
        actual = stack.pop()
        expected = CustomObject("3")
        assertEquals(expected, actual)

    }
}

private data class CustomObject(val id: String)
