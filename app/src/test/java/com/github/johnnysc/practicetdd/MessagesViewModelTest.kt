package com.github.johnnysc.practicetdd

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 05.01.2023
 */
class MessagesViewModelTest {
    @Test
    fun `test successful response`() = runBlocking {
        val testChainFactory = TestChainFactory(TestChainOne())
        testChainFactory.nextFeatureChain(featureChain = TestChainTwo())
        val communication = TestCommunication()
        val dispatchers = TestDispatchersList()
        val viewModel = MessagesViewModel(
            dispatchersList = dispatchers,
            communication = communication,
            viewModelChain = testChainFactory
        )
        viewModel.handleInput(message = "For first one")
        assertEquals(
            MessageUI.User(text = "For first one", id = "0"),
            communication.messages[0]
        )
        assertEquals(
            MessageUI.Ai(text = "First message", id = "1"),
            communication.messages[1]
        )
    }

    @Test
    fun `test error response`() = runBlocking {
        val testChainFactory = TestChainFactory(TestChainOne())
        testChainFactory.nextFeatureChain(featureChain = TestChainTwo())
        val communication = TestCommunication()
        val dispatchers = TestDispatchersList()
        val viewModel = MessagesViewModel(
            dispatchersList = dispatchers,
            communication = communication,
            viewModelChain = testChainFactory
        )
        viewModel.handleInput(message = "For second one")
        assertEquals(
            MessageUI.User(text = "For second one", id = "0"),
            communication.messages[0]
        )
        assertEquals(
            MessageUI.AiError(text = "I don't understand you", id = "1"),
            communication.messages[1]
        )
    }

    private class TestChainFactory(feature: FeatureChain.CheckAndHandle) : ViewModelChain(feature)

    private class TestChainOne : FeatureChain.CheckAndHandle {
        override fun canHandle(message: String): Boolean = message == "For first one"

        override suspend fun handle(message: String): MessageUI =
            MessageUI.Ai(text = "First message")
    }

    private class TestChainTwo : FeatureChain.Handle {
        override suspend fun handle(message: String): MessageUI =
            MessageUI.AiError(text = "I don't understand you")
    }

    private class TestCommunication : MessagesCommunication.Mutable {

        var messages: ArrayList<MessageUI> = MessagesArrayList()

        override fun map(data: MessageUI) {
            messages.add(data)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<List<MessageUI>>) = Unit
    }

    private class TestDispatchersList(
        private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()
    ) : DispatchersList {

        override fun launchUI(
            scope: CoroutineScope,
            block: suspend CoroutineScope.() -> Unit
        ): Job = scope.launch(dispatcher, block = block)

        override fun launchBackground(
            scope: CoroutineScope,
            block: suspend CoroutineScope.() -> Unit
        ): Job = scope.launch(dispatcher, block = block)

        override suspend fun changeToUI(block: suspend CoroutineScope. () -> Unit) =
            withContext(dispatcher, block)
    }
}