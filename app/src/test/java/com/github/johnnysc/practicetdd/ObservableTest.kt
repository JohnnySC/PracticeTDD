package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 28.12.2022
 */
class ObservableTest {

    @Test
    fun test_one_observer() {
        val observable = CustomObservable.Base<CustomObject, FakeObserver>()
        val fakeObserver = FakeObserver()
        observable.addObserver(observer = fakeObserver)
        observable.update(argument = CustomObject("one"))
        assertEquals(CustomObject("one"), fakeObserver.customObject)
    }

    @Test
    fun test_two_observers() {
        val observable = CustomObservable.Base<CustomObject, FakeObserver>()
        val fakeObserverOne = FakeObserver()
        val fakeObserverTwo = FakeObserver()
        observable.addObserver(observer = fakeObserverOne)
        observable.addObserver(observer = fakeObserverTwo)
        observable.update(argument = CustomObject("one"))
        assertEquals(CustomObject("one"), fakeObserverOne.customObject)
        assertEquals(CustomObject("one"), fakeObserverTwo.customObject)
    }

    @Test
    fun test_add_and_remove_observer() {
        val observable = CustomObservable.Base<CustomObject, FakeObserver>()
        val fakeObserver = FakeObserver()
        observable.addObserver(observer = fakeObserver)
        observable.update(argument = CustomObject("one"))
        assertEquals(CustomObject("one"), fakeObserver.customObject)

        observable.removeObserver(observer = fakeObserver)
        observable.update(argument = CustomObject("two"))
        assertEquals(CustomObject("one"), fakeObserver.customObject)
    }

    @Test
    fun test_add_and_remove_two_observers() {
        val observable = CustomObservable.Base<CustomObject, FakeObserver>()
        val fakeObserverOne = FakeObserver()
        val fakeObserverTwo = FakeObserver()
        observable.addObserver(observer = fakeObserverOne)
        observable.addObserver(observer = fakeObserverTwo)
        observable.update(argument = CustomObject("one"))
        assertEquals(CustomObject("one"), fakeObserverOne.customObject)
        assertEquals(CustomObject("one"), fakeObserverTwo.customObject)

        observable.removeObserver(observer = fakeObserverOne)
        observable.update(argument = CustomObject("two"))
        assertEquals(CustomObject("one"), fakeObserverOne.customObject)
        assertEquals(CustomObject("two"), fakeObserverTwo.customObject)
    }
}

private data class CustomObject(private val data: String)

private class FakeObserver : CustomObserver<CustomObject> {

    var customObject: CustomObject? = null
    override fun update(argument: CustomObject) {
        customObject = argument
    }
}