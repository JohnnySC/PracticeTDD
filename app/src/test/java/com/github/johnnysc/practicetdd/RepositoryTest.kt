package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 28.12.2022
 */
class RepositoryTest {

    @Test
    fun `test success`() {
        val api = FakeApi()
        val repository = Repository.Base(api = api)

        repository.fetch("not empty body here", object : Repository.DataCallback {
            override fun provideSuccess(data: String) {
                assertEquals("successful response", data)
            }

            override fun provideError(message: String) = Unit
        })
    }

    @Test
    fun `test error`() {
        val api = FakeApi()
        val repository = Repository.Base(api = api)

        repository.fetch("", object : Repository.DataCallback {
            override fun provideSuccess(data: String) = Unit

            override fun provideError(message: String) {
                assertEquals("empty request body!", message)
            }
        })
    }
}

private class FakeApi : Api {
    override fun fetch(body: Api.RequestBody, callback: Api.Callback) {
        if (body.isEmpty())
            callback.provideError(Api.Result.Error(e = IllegalStateException("empty request body!")))
        else
            callback.provideSuccess(Api.Result.Success(data = "successful response"))
    }
}