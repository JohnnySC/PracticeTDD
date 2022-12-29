package com.github.johnnysc.practicetdd

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 29.12.2022
 */
class ChainTest {

    @Test
    fun test_first_chain_can_handle(): Unit = runBlocking {
        val chain = ViewModelChain(featureChain = FakeChainOne())
        val featureChain = FakeChainTwo()
        chain.nextFeatureChain(nextFeatureChain = featureChain)
        val actual: MessageUI = chain.handle(message = "")
        val expected = FakeMessageUi("two ")
        assertEquals(expected, actual)
    }

    @Test
    fun test_cannot_first_chain_cannot_handle(): Unit = runBlocking {
        val chain = ViewModelChain(featureChain = FakeChainOne())
        val featureChain = FakeChainTwo()
        chain.nextFeatureChain(featureChain)
        val actual: MessageUI = chain.handle(message = "should handle first")
        val expected = FakeMessageUi("one should handle first")
        assertEquals(expected, actual)
    }

    @Test
    fun test_next_chain_not_set(): Unit = runBlocking {
        val chain = ViewModelChain(featureChain = FakeChainOne())
        val actual: MessageUI = chain.handle(message = "")
        val expected = MessageUI.Empty
        assertEquals(expected, actual)
    }
}

private class FakeChainOne : FeatureChain.CheckAndHandle {
    override fun canHandle(message: String): Boolean {
        return message.isNotEmpty()
    }

    override suspend fun handle(message: String): MessageUI {
        return FakeMessageUi("one $message")
    }
}

private class FakeChainTwo : FeatureChain.Handle {

    override suspend fun handle(message: String): MessageUI {
        return FakeMessageUi("two $message")
    }
}

private data class FakeMessageUi(val value: String) : MessageUI
