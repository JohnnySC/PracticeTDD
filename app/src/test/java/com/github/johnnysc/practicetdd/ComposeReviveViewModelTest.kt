package com.github.johnnysc.practicetdd

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.Serializable

class ComposeReviveViewModelTest {

    private lateinit var repository: FakeSimpleRepository
    private lateinit var viewModel: ComposeReviveViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        repository = FakeSimpleRepository()
        savedStateHandle = SavedStateHandle()
        viewModel = ComposeReviveViewModel(
            runAsync = FakeRunAsync(),
            repository = repository,
            mapper = FakeMapper(),
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun success() {
        repository.expectSuccess()
        val state: StateFlow<UiState> = viewModel.state
        val actualState: UiState = state.value
        assertEquals(UiState.Loading, actualState)

        viewModel.load()

        assertEquals(
            FakeSuccessUiState(DateAndName(date = "fakeDate", name = "fakeName")),
            viewModel.state.value
        )
    }

    @Test
    fun `failed then retry to get success`() {
        repository.expectError()
        assertEquals(UiState.Loading, viewModel.state.value)

        viewModel.load()

        assertEquals(
            FakeFailureUiState(errorMessage = "no connection!"),
            viewModel.state.value
        )

        repository.expectSuccess()
        viewModel.retry()
        assertEquals(UiState.Loading, viewModel.state.value)

        viewModel.load()
        assertEquals(
            FakeSuccessUiState(DateAndName(date = "fakeDate", name = "fakeName")),
            viewModel.state.value
        )
    }

    @Test
    fun `revive process death`() {
        repository.expectError()
        assertEquals(UiState.Loading, viewModel.state.value)

        viewModel.load()

        assertEquals(
            FakeFailureUiState(errorMessage = "no connection!"),
            viewModel.state.value
        )

        //process death happening here, everything is new except SavedStateHandle
        repository = FakeSimpleRepository()
        viewModel = ComposeReviveViewModel(
            runAsync = FakeRunAsync(),
            repository = repository,
            mapper = FakeMapper(),
            savedStateHandle = savedStateHandle
        )

        assertEquals(
            FakeFailureUiState(errorMessage = "no connection!"),
            viewModel.state.value
        )

        repository.expectSuccess()
        viewModel.retry()
        assertEquals(UiState.Loading, viewModel.state.value)

        viewModel.load()
        assertEquals(
            FakeSuccessUiState(DateAndName(date = "fakeDate", name = "fakeName")),
            viewModel.state.value
        )
    }
}

private class FakeSimpleRepository : SimpleRepository {

    private lateinit var result: LoadResult

    fun expectSuccess() {
        result = FakeSuccessResult()
    }

    fun expectError() {
        result = FakeErrorResult()
    }

    override suspend fun data(): LoadResult {
        return result
    }
}

private class FakeSuccessResult : LoadResult {
    override fun <T : Serializable> map(mapper: LoadResult.Mapper<T>): T {
        return mapper.map(DateAndName(date = "fakeDate", name = "fakeName"))
    }
}

private class FakeErrorResult : LoadResult {
    override fun <T : Serializable> map(mapper: LoadResult.Mapper<T>): T {
        return mapper.map(FakeError())
    }
}

private class FakeError : Exception("no connection!")

private class FakeRunAsync : RunAsync {
    override fun <T : Any> async(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit
    ) = runBlocking {
        ui.invoke(background.invoke())
    }
}

private class FakeMapper : LoadResult.Mapper<UiState> {

    override fun map(data: DateAndName): UiState {
        return FakeSuccessUiState(data)
    }

    override fun map(error: Exception): UiState {
        return FakeFailureUiState(error.message ?: error.toString())
    }
}

private data class FakeSuccessUiState(private val data: DateAndName) : UiState
private data class FakeFailureUiState(private val errorMessage: String) : UiState