package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 28.12.2022
 */
class ObservableTest {

    @Test
    fun test_premium() {
        val observable = CustomObservable.Base<Video, VideoObserver>()
        val usualObserver = FakeUsualObserver()
        val premiumObserver = FakePremiumObserver()
        observable.addObserver(usualObserver)
        observable.addObserver(premiumObserver)
        observable.update(Video.Premium("premiumLink"))
        assertEquals(Video.Usual(""), usualObserver.obj)
        assertEquals(Video.Premium("premiumLink"), premiumObserver.obj)
    }

    @Test
    fun test_not_premium() {
        val observable = CustomObservable.Base<Video, VideoObserver>()
        val usualObserver = FakeUsualObserver()
        val premiumObserver = FakePremiumObserver()
        observable.addObserver(usualObserver)
        observable.addObserver(premiumObserver)
        observable.update(Video.Usual("usualLink"))
        assertEquals(Video.Usual("usualLink"), usualObserver.obj)
        assertEquals(Video.Usual("usualLink"), premiumObserver.obj)
    }
}

private interface VideoObserver : CustomObserver<Video>

private class FakeUsualObserver : CustomObserver.Usual<Video>(), VideoObserver {

    var obj: Video = Video.Usual("")

    override fun update(obj: Video) {
        this.obj = obj
    }
}

private class FakePremiumObserver : CustomObserver.Premium<Video>(), VideoObserver {

    var obj: Video = Video.Premium("")

    override fun update(obj: Video) {
        this.obj = obj
    }
}

private interface Video : CustomObject {
    data class Premium(private val link: String) : CustomObject.Premium(), Video
    data class Usual(private val link: String) : CustomObject.Usual(), Video
}