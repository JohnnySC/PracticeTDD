package com.github.johnnysc.practicetdd

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class Task047RunAsyncList {

    private lateinit var runAsync: FakeRunAsync
    private lateinit var repository: FakeRepository
    private lateinit var viewModel: ComplexViewModel

    @Before
    fun setup() {
        runAsync = FakeRunAsync()
        repository = FakeRepository()
        viewModel = ComplexViewModel(runAsync = runAsync, repository = repository)
    }

    @Test
    fun first_fun() {
        assertEquals(UiState.Initial, viewModel.uiState.value)

        viewModel.firstFun()
        assertEquals(UiState.Loading, viewModel.uiState.value)

        runAsync.returnResult(position = 0)
        assertEquals(UiState.Success(text = "A"), viewModel.uiState.value)
    }

    @Test
    fun second_fun() {
        assertEquals(UiState.Initial, viewModel.uiState.value)

        viewModel.secondFun()
        assertEquals(UiState.Loading, viewModel.uiState.value)

        runAsync.returnResult(position = 0)
        assertEquals(UiState.Success(text = "B"), viewModel.uiState.value)
    }

    @Test
    fun first_fun_then_second_fun() {
        assertEquals(UiState.Initial, viewModel.uiState.value)

        viewModel.firstFun()
        assertEquals(UiState.Loading, viewModel.uiState.value)

        runAsync.returnResult(position = 0)
        assertEquals(UiState.Success(text = "A"), viewModel.uiState.value)

        viewModel.secondFun()
        assertEquals(UiState.Loading, viewModel.uiState.value)

        runAsync.returnResult(position = 1)
        assertEquals(UiState.Success(text = "AB"), viewModel.uiState.value)
    }

    @Test
    fun second_fun_then_first_one() {
        assertEquals(UiState.Initial, viewModel.uiState.value)

        viewModel.secondFun()
        assertEquals(UiState.Loading, viewModel.uiState.value)

        runAsync.returnResult(position = 0)
        assertEquals(UiState.Success(text = "B"), viewModel.uiState.value)

        viewModel.firstFun()
        assertEquals(UiState.Loading, viewModel.uiState.value)

        runAsync.returnResult(position = 1)
        assertEquals(UiState.Success(text = "AB"), viewModel.uiState.value)
    }

    @Test
    fun first_fun_in_parallel_second_one() {
        assertEquals(UiState.Initial, viewModel.uiState.value)

        viewModel.firstFun()
        assertEquals(UiState.Loading, viewModel.uiState.value)

        viewModel.secondFun()
        assertEquals(UiState.Loading, viewModel.uiState.value)

        runAsync.returnResult(position = 0)
        assertEquals(UiState.Loading, viewModel.uiState.value)

        runAsync.returnResult(position = 1)
        assertEquals(UiState.Success(text = "AB"), viewModel.uiState.value)
    }

    @Test
    fun second_fun_finish_earlier_than_first() {
        assertEquals(UiState.Initial, viewModel.uiState.value)

        viewModel.firstFun()
        assertEquals(UiState.Loading, viewModel.uiState.value)

        viewModel.secondFun()
        assertEquals(UiState.Loading, viewModel.uiState.value)

        runAsync.returnResult(position = 1)
        assertEquals(UiState.Loading, viewModel.uiState.value)

        runAsync.returnResult(position = 0)
        assertEquals(UiState.Success(text = "AB"), viewModel.uiState.value)
    }
}

private class FakeRunAsync : RunAsync {

    private val cachedResultList: MutableList<Any> = mutableListOf()
    private val uiCachedList: MutableList<(Any) -> Unit> = mutableListOf()

    override fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit
    ) {
        scope.launch(Dispatchers.Unconfined) {
            cachedResultList.add(background.invoke())
            uiCachedList.add(ui as (Any) -> Unit)
        }
    }

    fun returnResult(position: Int) {
        uiCachedList[position].invoke(cachedResultList[position])
    }
}

private class FakeRepository : ComplexRepository {

    override suspend fun first(): String {
        return "A"
    }

    override suspend fun second(): String {
        return "B"
    }
}