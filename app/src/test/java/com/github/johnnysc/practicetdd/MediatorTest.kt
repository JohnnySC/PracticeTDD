package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Also check out Mediator Test in androidTest package with Espresso
 */
class MediatorTest {

    @Test
    fun test() {
        val mediator = Mediator.Base()
        val choiceOne = FakeChoice()
        val choiceTwo = FakeChoice()
        var count = 0
        val block: () -> Unit = { count++ }

        mediator.change(choiceOne, block)

        assertEquals(0, choiceOne.rollbackCalledCount)
        assertEquals(1, choiceOne.choseCalledCount)
        assertEquals(1, count)

        mediator.change(choiceTwo, block)

        assertEquals(1, choiceOne.rollbackCalledCount)
        assertEquals(1, choiceOne.choseCalledCount)
        assertEquals(2, count)
        assertEquals(1, choiceTwo.choseCalledCount)
        assertEquals(0, choiceTwo.rollbackCalledCount)

        mediator.change(choiceOne, block)
        assertEquals(1, choiceOne.rollbackCalledCount)
        assertEquals(2, choiceOne.choseCalledCount)
        assertEquals(3, count)
        assertEquals(1, choiceTwo.choseCalledCount)
        assertEquals(1, choiceTwo.rollbackCalledCount)
    }
}

private class FakeChoice : Choice {

    override fun init(mediator: Mediator, block: () -> Unit) = Unit

    override fun isChosen() =
        choseCalledCount != 0 && rollbackCalledCount != 0 && choseCalledCount > rollbackCalledCount

    var choseCalledCount = 0

    override fun chose() {
        choseCalledCount++
    }

    var rollbackCalledCount = 0

    override fun rollback() {
        rollbackCalledCount++
    }
}