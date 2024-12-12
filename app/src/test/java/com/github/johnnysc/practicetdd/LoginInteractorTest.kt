package com.github.johnnysc.practicetdd

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * User try to log in by inputting login and password
 * 2 results: success and error
 * Types of error (not blocking aka technical):
 * 1. no internet connection
 * 2. server is not available
 *
 * Type of error (user based blocking)
 * 1. incorrect login and/or password
 *
 * business logic error: 3 attempts to login with incorrect credentials: should block user
 */
class LoginInteractorTest {

    private lateinit var repository: FakeRepository
    private lateinit var interactor: LoginInteractor

    @Before
    fun setup() {
        repository = FakeRepository.Base()
        interactor = LoginInteractor.Base(repository = repository)
    }

    @Test
    fun success() = runBlocking {
        repository.expectSuccess()

        val actual: LoginResult =
            interactor.login(
                credentials = LoginCredentials(
                    login = "validMail@name.com",
                    password = "correctPassword"
                )
            )

        val expected: LoginResult = LoginResult.Success

        assertEquals(expected, actual)
        repository.assertLoginCalled(1)
    }

    @Test
    fun success_after_three_technical_errors() = runBlocking {
        repository.expectError(LoginResult.Error.NoInternet)

        var actual: LoginResult =
            interactor.login(
                credentials = LoginCredentials(
                    login = "validMail@name.com",
                    password = "correctPassword"
                )
            )

        var expected: LoginResult = LoginResult.Error.NoInternet

        assertEquals(expected, actual)

        repository.expectError(LoginResult.Error.ServerUnavailable)

        actual =
            interactor.login(
                credentials = LoginCredentials(
                    login = "validMail@name.com",
                    password = "correctPassword"
                )
            )
        expected = LoginResult.Error.ServerUnavailable
        assertEquals(expected, actual)

        repository.expectError(LoginResult.Error.NoInternet)

        actual =
            interactor.login(
                credentials = LoginCredentials(
                    login = "validMail@name.com",
                    password = "correctPassword"
                )
            )

        expected = LoginResult.Error.NoInternet

        assertEquals(expected, actual)

        repository.expectSuccess()

        actual =
            interactor.login(
                credentials = LoginCredentials(
                    login = "validMail@name.com",
                    password = "correctPassword"
                )
            )

        expected = LoginResult.Success

        assertEquals(expected, actual)
        repository.assertLoginCalled(4)
    }

    @Test
    fun blocking_user_after_3_incorrect_credentials() = runBlocking {
        repository.expectError(LoginResult.Error.IncorrectCredentials)

        var actual: LoginResult =
            interactor.login(
                credentials = LoginCredentials(
                    login = "invalidMail@name.com",
                    password = "correctPassword"
                )
            )
        var expected: LoginResult = LoginResult.Error.IncorrectCredentials

        assertEquals(expected, actual)

        repository.expectError(LoginResult.Error.NoInternet)

        actual =
            interactor.login(
                credentials = LoginCredentials(
                    login = "invalidMail@name.com",
                    password = "incorrectPassword"
                )
            )

        expected = LoginResult.Error.NoInternet

        assertEquals(expected, actual)

        repository.expectError(LoginResult.Error.IncorrectCredentials)

        actual =
            interactor.login(
                credentials = LoginCredentials(
                    login = "invalidMail@name.com",
                    password = "incorrectPassword"
                )
            )

        expected = LoginResult.Error.IncorrectCredentials

        assertEquals(expected, actual)

        actual =
            interactor.login(
                credentials = LoginCredentials(
                    login = "validMail@name.com",
                    password = "incorrectPassword"
                )
            )

        expected = LoginResult.Error.IncorrectCredentials

        assertEquals(expected, actual)
        repository.assertLoginCalled(4)

        repository.expectSuccess()

        actual =
            interactor.login(
                credentials = LoginCredentials(
                    login = "validMail@name.com",
                    password = "incorrectPassword"
                )
            )

        expected = LoginResult.Block

        assertEquals(expected, actual)
        repository.assertLoginCalled(4)
    }
}

private interface FakeRepository : LoginRepository {

    fun expectSuccess()

    fun expectError(error: LoginResult.Error)

    fun assertLoginCalled(expectedTimes: Int)

    class Base : FakeRepository {

        private lateinit var result: LoginResult
        private var loginCalledTimes = 0
        private var error: LoginResult.Error? = null

        override fun expectSuccess() {
            error = null
            this.result = LoginResult.Success
        }

        override fun expectError(error: LoginResult.Error) {
            this.error = error
        }

        override fun assertLoginCalled(expectedTimes: Int) {
            assertEquals(expectedTimes, loginCalledTimes)
        }

        override suspend fun login(credentials: LoginCredentials): LoginResult {
            loginCalledTimes++
            error?.let {
                throw it
            }
            return result
        }
    }
}