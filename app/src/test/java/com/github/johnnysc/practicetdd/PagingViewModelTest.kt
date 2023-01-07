package com.github.johnnysc.practicetdd

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 07.01.2023
 */
class PagingViewModelTest {

    @Test
    fun test() {
        val communication = FakeCommunication()
        val repository = FakeRepository()
        val viewModel = PagingViewModel(repository, communication)

        viewModel.init(isFirstRun = true)
        var expected = listOf(
            MessageUi.Base(id = 0, "text of message 0"),
            MessageUi.Base(id = 1, "text of message 1"),
            MessageUi.LoadMore
        )
        assertEquals(expected, communication.list)

        viewModel.loadMore()
        expected = listOf(
            MessageUi.LoadPrevious,
            MessageUi.Base(id = 2, "text of message 2"),
            MessageUi.Base(id = 3, "text of message 3"),
            MessageUi.LoadMore
        )
        assertEquals(expected, communication.list)

        viewModel.loadMore()
        expected = listOf(
            MessageUi.LoadPrevious,
            MessageUi.Base(id = 4, "text of message 4"),
            MessageUi.Base(id = 5, "text of message 5")
        )
        assertEquals(expected, communication.list)

        viewModel.loadPrevious()
        expected = listOf(
            MessageUi.LoadPrevious,
            MessageUi.Base(id = 2, "text of message 2"),
            MessageUi.Base(id = 3, "text of message 3"),
            MessageUi.LoadMore
        )
        assertEquals(expected, communication.list)

        viewModel.loadPrevious()
        expected = listOf(
            MessageUi.Base(id = 0, "text of message 0"),
            MessageUi.Base(id = 1, "text of message 1"),
            MessageUi.LoadMore
        )
        assertEquals(expected, communication.list)
    }
}

private class FakeRepository : PagingRepository {

    private var page = 0

    override fun messages(strategy: PagingRepository.Strategy): List<MessageDomain> {
        if (strategy == PagingRepository.Strategy.NEXT) {
            page++
        } else if (strategy == PagingRepository.Strategy.PREVIOUS) {
            page--
        }
        val list = mutableListOf<MessageDomain>()
        if (page > 0) {
            list.add(MessageDomain.LoadPrevious)
        }
        for (i in 0..1) {
            list.add(
                MessageDomain.Base(
                    id = (page * SIZE) + i,
                    text = "text of message ${(page * SIZE) + i}"
                )
            )
        }
        if (page + 1 < MAXIMUM_PAGES)
            list.add(MessageDomain.LoadMore)
        return list
    }

    companion object {
        private const val SIZE = 2
        private const val MAXIMUM_PAGES = 3
    }
}

private class FakeCommunication : Communication {

    var list = listOf<MessageUi>()

    override fun map(data: List<MessageUi>) {
        list = data
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<List<MessageUi>>) = Unit
}