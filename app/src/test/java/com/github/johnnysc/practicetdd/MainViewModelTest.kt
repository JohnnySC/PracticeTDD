package com.github.johnnysc.practicetdd

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 02.01.2023
 */
class MainViewModelTest {

    @Test
    fun test_success() {
        val repository = FakeRepository(true)
        val communication = FakeCommunication()
        val mainViewModel = MainViewModel(
            repository = repository,
            communication = communication,
            schedulersList = TestSchedulersList()
        )
        mainViewModel.fetch()
        assertEquals("success", communication.value)
    }

    @Test
    fun test_error() {
        val repository = FakeRepository(false)
        val communication = FakeCommunication()
        val mainViewModel = MainViewModel(
            repository,
            communication,
            TestSchedulersList()
        )
        mainViewModel.fetch()
        assertEquals("network problem", communication.value)
    }
}

private class FakeRepository(private val success: Boolean) : Repository {
    override fun fetch(): Single<String> = if (success)
        Single.just("success")
    else
        Single.error(IllegalStateException("network problem"))
}

private class FakeCommunication : Communication {
    var value: String = ""

    override fun observe(owner: LifecycleOwner, observer: Observer<String>) = Unit

    override fun map(data: String) {
        value = data
    }
}

private class TestSchedulersList : SchedulersList {
    private val sheduler = Schedulers.trampoline()
    override fun io(): Scheduler = sheduler
    override fun ui(): Scheduler = sheduler
}