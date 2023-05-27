package com.github.johnnysc.practicetdd

import junit.framework.TestCase.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 27.05.2023
 */
class TwoFieldsObservableTest {

    @Test
    fun test() {
        val observer = FakeTwoFieldsObjectObserver()
        val observable = TwoFieldsObservable(observer = observer)

        observable.accept(name = "firstName")
        observer.checkNotifyCalledTimes(0)
        observable.accept(id = 1)
        assertEquals(TwoFieldsObject(name = "firstName", id = 1), observer.list[0])
        observer.checkNotifyCalledTimes(1)

        observable.accept(id = 2)
        observer.checkNotifyCalledTimes(1)
        observable.accept(name = "secondName")
        assertEquals(TwoFieldsObject(name = "secondName", id = 2), observer.list[1])
        observer.checkNotifyCalledTimes(2)
    }

    private class FakeTwoFieldsObjectObserver : TwoFieldsObjectObserver {

        fun checkNotifyCalledTimes(timesCalled: Int) {
            assertEquals(timesCalled, list.size)
        }

        val list = mutableListOf<TwoFieldsObject>()

        override fun notify(obj: TwoFieldsObject) {
            list.add(obj)
        }
    }
}