package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

class StatePatternTest {

    @Test
    fun test() {
        val logging = FakeLogging()
        val stateContext: StateContext.Actions = StateContext.Base(state = FakeInitialState)
        stateContext.log(logging)
        logging.assertEvent("FakeInitialState")

        stateContext.next()
        stateContext.log(logging)

        logging.assertEvent("RequireLogin")

        stateContext.next()
        stateContext.log(logging)
        logging.assertEvent("LoginSuccessfully")
    }
}

private class FakeLogging : Logging {

    private var index = 0

    fun assertEvent(expected: String) {
        assertEquals(expected, events[index++])
    }

    private val events = mutableListOf<String>()

    override fun log(event: String) {
        events.add(event)
    }
}

private data object FakeInitialState : State {

    override fun next(context: StateContext.Update) {
        context.updateState(State.RequireLogin)
    }
}