package com.github.johnnysc.practicetdd

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 29.12.2022
 */
class CommandTest {

    @Test
    fun test_can_handle(): Unit = runBlocking {
        val command = TestCommand()
        val actual: Boolean = command.canHandle(message = "not empty message")
        val expected = true
        assertEquals(expected, actual)
        val interactor = TestInteractor.Base()
        val actualMessageUi = command.handle(useCase = interactor)
        val expectedMessageUi = FakeMessageUi("not empty message")
        assertEquals(expectedMessageUi, actualMessageUi)
    }

    @Test
    fun test_cannot_handle(): Unit = runBlocking {
        val command = TestCommand()
        val actual: Boolean = command.canHandle(message = "")
        val expected = false
        assertEquals(expected, actual)
        val interactor = TestInteractor.Base()
        val actualMessageUi = command.handle(useCase = interactor)
        val expectedMessageUi = MessageUI.Empty
        assertEquals(expectedMessageUi, actualMessageUi)
    }
}

private class TestCommand : Command.Abstract<TestInteractor, TestUseCase>(parser = TestParser())

private class TestParser : Parser<TestUseCase> {

    override fun map(data: String): IsEmptyHandleUseCase<TestUseCase> {
        return if (data.isEmpty())
            IsEmptyHandleUseCase.Empty()
        else
            TestHandleUseCase(data)
    }
}

private class TestHandleUseCase(private val message: String) : IsEmptyHandleUseCase<TestUseCase> {
    override suspend fun handle(useCase: TestUseCase): MessageUI {
        return useCase.data(message)
    }
}

private interface TestUseCase {
    fun data(message: String): MessageUI
}

private interface TestInteractor : TestUseCase {
    class Base : TestInteractor {
        override fun data(message: String): MessageUI {
            return FakeMessageUi(message)
        }
    }
}

private data class FakeMessageUi(val text: String) : MessageUI