package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 17.01.2023
 */
class MainViewModelTest {

    @Test
    fun test() {
        val myLiveData: MyObservable<String> = MyObservable.SingleLiveEvent()
        val observer = FakeObserver()
        val viewModel = MainViewModel(liveData = myLiveData)
        viewModel.updateObserver(observer = observer)
        viewModel.go()
        assertEquals("1", observer.list[0])
        assertEquals(1, observer.list.size)
        viewModel.go()
        assertEquals("2", observer.list[1])
        assertEquals(2, observer.list.size)
        viewModel.updateObserver(MyObserver.Empty())
        viewModel.go()
        assertEquals(2, observer.list.size)
        viewModel.updateObserver(observer)
        viewModel.notifyChanges()
        assertEquals("3", observer.list[2])
        assertEquals(3, observer.list.size)
    }
}

private class FakeObserver : MyObserver<String> {
    val list = ArrayList<String>()
    override fun update(value: String) {
        list.add(value)
    }
}
