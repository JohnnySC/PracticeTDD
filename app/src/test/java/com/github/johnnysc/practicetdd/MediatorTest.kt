package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

class MediatorTest {

    @Test
    fun test() {
        val mediator: Mediator = Mediator.Base()
        val userOne = FakeUser(id = "1", mediator = mediator)
        val userTwo = FakeUser(id = "2", mediator = mediator)
        val userThree = FakeUser(id = "3", mediator = mediator)
        mediator.addUser(user = userOne)
        mediator.addUser(user = userTwo)
        mediator.addUser(user = userThree)

        userOne.send(message = "message from user one")
        userOne.checkReceiveCalledTimes(times = 0)

        userTwo.checkReceived(message = "message from user one")
        userTwo.checkReceiveCalledTimes(times = 1)
        userThree.checkReceived(message = "message from user one")
        userThree.checkReceiveCalledTimes(times = 1)

        userTwo.send(message = "message from user two")
        userTwo.checkReceiveCalledTimes(times = 1)

        userOne.checkReceived(message = "message from user two")
        userOne.checkReceiveCalledTimes(times = 1)
        userThree.checkReceived(message = "message from user two")
        userThree.checkReceiveCalledTimes(times = 2)

        userThree.send(message = "message from user three")
        userThree.checkReceiveCalledTimes(times = 2)

        userOne.checkReceived(message = "message from user three")
        userOne.checkReceiveCalledTimes(times = 2)
        userTwo.checkReceived(message = "message from user three")
        userTwo.checkReceiveCalledTimes(times = 2)
    }
}

private class FakeUser(private val id: String, mediator: Mediator) :
    User.Abstract(mediator = mediator) {

    private var count = 0
    private var receivedMessage: String = ""

    override fun receive(message: String) {
        receivedMessage = message
        count++
    }

    fun checkReceived(message: String) {
        assertEquals(message, receivedMessage)
    }

    fun checkReceiveCalledTimes(times: Int) {
        assertEquals(times, count)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FakeUser

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
