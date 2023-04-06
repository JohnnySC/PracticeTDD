package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 06.04.2023
 */
class ReviewViewModelTest {

    @Test
    fun `test revive`() {
        val bundle = FakeSaveAndRestore()

        val communication = FakeCommunication()
        val viewModel = ReviveViewModel(communication = communication)

        //first start of the process
        viewModel.restore(bundle)
        assertEquals(true, bundle.isEmpty())
        assertEquals(ReviveUiState.Initial, communication.value)

        //change ui state
        viewModel.show(text = "some input")
        assertEquals(ReviveUiState.Secondary(text = "some input"), communication.value)

        //imitate death of the process
        viewModel.save(bundle)
        assertEquals(false, bundle.isEmpty())
        val newCommunication = FakeCommunication()
        val newViewModel = ReviveViewModel(communication = newCommunication)

        //second start of the process
        newViewModel.restore(bundle)
        assertEquals(ReviveUiState.Secondary(text = "some input"), newCommunication.value)
    }
}

private class FakeSaveAndRestore : SaveAndRestore {

    private var bundle: ReviveUiState? = null

    override fun isEmpty(): Boolean {
        return bundle == null
    }

    override fun save(state: ReviveUiState) {
        bundle = state
    }

    override fun restore(): ReviveUiState {
        return bundle!!
    }
}

private class FakeCommunication : ReviveCommunication {

    var value: ReviveUiState? = null

    override fun map(value: ReviveUiState) {
        this.value = value
    }

    override fun save(saveAndRestore: SaveAndRestore) {
        value?.let {
            saveAndRestore.save(it)
        }
    }
}