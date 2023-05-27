package com.github.johnnysc.practicetdd

import junit.framework.TestCase.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 27.05.2023
 */
class TwoFieldsObservableTest {

    @Test
    fun test() {
        val observable = FakeTwoFieldsObjectObserver()
        val observer = TwoFieldsObservable(observable = observable)

        observer.accept(name = "firstName")
        observable.checkNotifyCalledTimes(0)
        observer.accept(id = 1)
        assertEquals(TwoFieldsObject(name = "firstName", id = 1), observable.list[0])
        observable.checkNotifyCalledTimes(1)

        observer.accept(id = 2)
        observable.checkNotifyCalledTimes(1)
        observer.accept(name = "secondName")
        assertEquals(TwoFieldsObject(name = "secondName", id = 2), observable.list[1])
        observable.checkNotifyCalledTimes(2)
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