package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 28.02.2023
 */
class NodeBuilderTest {

    @Test
    fun `test head`() {
        val builder: MyNode.Builder = MyNode.Builder()
        val actual: MyNode = builder.build()
        val expected: MyNode = MyNode.Head(id = 0, value = "")
        assertEquals(expected, actual)
        assertEquals(false, actual.hasParent())
    }

    @Test
    fun `test two items`() {
        val builder: MyNode.Builder = MyNode.Builder()
        var actual: MyNode = builder
            .value(value = "header")
            .build()
        var expected: MyNode = MyNode.Head(id = 0, value = "header")
        assertEquals(expected, actual)
        assertEquals(false, actual.hasParent())

        actual = builder
            .value(value = "item one")
            .build()
        expected = MyNode.Base(id = 1, parent = MyNode.Head(id = 0), value = "item one")
        assertEquals(expected, actual)
        assertEquals(true, actual.hasParent())
    }

    @Test
    fun `test three items`() {
        val builder: MyNode.Builder = MyNode.Builder()
        var actual: MyNode = builder
            .value(value = "header")
            .build()
        var expected: MyNode = MyNode.Head(id = 0, value = "header")
        assertEquals(expected, actual)
        assertEquals(false, actual.hasParent())

        actual = builder
            .value(value = "item one")
            .build()
        expected = MyNode.Base(id = 1, parent = MyNode.Head(id = 0), value = "item one")
        assertEquals(expected, actual)
        assertEquals(true, actual.hasParent())

        actual = builder
            .value(value = "item two")
            .build()
        expected = MyNode.Base(
            id = 2,
            parent = MyNode.Base(id = 1, parent = MyNode.Head(id = 0), value = "item one"),
            value = "item two"
        )
        assertEquals(expected, actual)
        assertEquals(true, actual.hasParent())
    }
}