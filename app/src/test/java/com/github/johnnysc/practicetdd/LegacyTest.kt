package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * @author Asatryan on 04.01.2023
 */
class LegacyTest {

    @Test
    fun test() {
        val actual: LegacyObject = Legacy(text = "test", interaction = FakeInteraction).map()
        val lambda: () -> Unit = HandleInteraction(text = "test", interaction = FakeInteraction)
        val expected = LegacyObject(text = "test", lambda = lambda)
        assertEquals(expected, actual)

        actual.go()
        assertEquals("test", FakeInteraction.printCalledWithValue)
    }
}

private object FakeInteraction : Interaction {
    var printCalledWithValue = ""

    override fun print(value: String) {
        printCalledWithValue = value
    }
}