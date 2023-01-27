package com.github.johnnysc.practicetdd

import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

/**
 * @author Asatryan on 27.01.2023
 */
class CloudDataSourceTest {

    private lateinit var service: FakeService
    private lateinit var cloudDataSource: CloudDataSource

    @Before
    fun setUp() {
        service = FakeService()
        cloudDataSource = CloudDataSource.Base(apiService = service)
    }

    @Test
    fun test_200() = runBlocking {
        service.response =
            Response.success("{ \"name\": \"userName\", \"id\": 1001 }".toResponseBody("application/json".toMediaType()))

        val actual: UserCloud = cloudDataSource.fetch()
        assertEquals(UserCloud.Base(name = "userName", id = 1001), actual)
    }

    @Test
    fun test_401() {
        service.response = Response.error(
            401,
            "{ \"errorMessage\": \"Unauthorized user\", \"errorType\": \"UNAUTHORIZED\" }".toResponseBody(
                "application/json".toMediaType()
            )
        )

        try {
            cloudDataSource.fetch()
        } catch (e: Exception) {
            assertEquals(
                ServerException(message = "Unauthorized user", errorType = "UNAUTHORIZED"),
                e
            )
        }
    }

    @Test
    fun test_404() {
        service.response = Response.error(
            404,
            "{ \"errorMessage\": \"not found\" }".toResponseBody(
                "application/json".toMediaType()
            )
        )
        try {
            cloudDataSource.fetch()
        } catch (e: Exception) {
            assertEquals(ServerException(message = "not found"), e)
        }
    }
}

private class FakeService : ApiService {

    var response: Response<UserCloud>? = null

    override suspend fun fetch(): Response<UserCloud> {
        return response!!
    }
}
