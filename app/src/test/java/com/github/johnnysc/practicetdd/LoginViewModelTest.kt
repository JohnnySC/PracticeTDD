package com.github.johnnysc.practicetdd

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    private lateinit var uiUpdate: FakeLoginUpdate
    private lateinit var validateLogin: FakeValidateLogin
    private lateinit var validatePassword: FakeValidatePassword
    private lateinit var interactor: FakeLoginInteractor
    private lateinit var runAsync: FakeRunAsync
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        uiUpdate = FakeLoginUpdate.Base()
        validateLogin = FakeValidateLogin.Base()
        validatePassword = FakeValidatePassword.Base()
        interactor = FakeLoginInteractor.Base()
        runAsync = FakeRunAsync.Base()
        viewModel = LoginViewModel(
            uiUpdate = uiUpdate,
            validateLogin = validateLogin,
            validatePassword = validatePassword,
            interactor = interactor,
            runAsync = runAsync
        )
    }

    @Test
    fun `incorrect login`() {
        validateLogin.expectIncorrect()

        viewModel.login(login = "testLogin", password = "testPassword")

        validateLogin.checkCalledWith(userInput = "testLogin")
        validatePassword.checkCalledTimes(times = 0)
        interactor.checkCalledTimes(times = 0)
        uiUpdate.checkCalledWith(result = LoginState.LoginError(message = "Incorrect login"))
    }

    @Test
    fun `incorrect password`() {
        validateLogin.expectCorrect()
        validatePassword.expectIncorrect()

        viewModel.login(login = "testLogin", password = "testPassword")

        validateLogin.checkCalledWith(userInput = "testLogin")
        validatePassword.checkCalledWith(userInput = "testPassword")
        interactor.checkCalledTimes(times = 0)
        uiUpdate.checkCalledWith(result = LoginState.PasswordError(message = "Incorrect password"))
    }

    @Test
    fun `no connection`() {
        validateLogin.expectCorrect()
        validatePassword.expectCorrect()
        interactor.expectFailed()

        viewModel.login(login = "testLogin", password = "testPassword")

        validateLogin.checkCalledWith(userInput = "testLogin")
        validatePassword.checkCalledWith(userInput = "testPassword")
        interactor.checkCalledTimes(times = 1)
        interactor.checkCalledWith(login = "testLogin", password = "testPassword")
        uiUpdate.checkCalledWith(result = LoginState.Loading)

        runAsync.returnResult()
        uiUpdate.checkCalledWith(result = LoginState.Failed(message = "No Internet"))
    }

    @Test
    fun successful() {
        validateLogin.expectCorrect()
        validatePassword.expectCorrect()
        interactor.expectSuccess()

        viewModel.login(login = "testLogin", password = "testPassword")

        validateLogin.checkCalledWith(userInput = "testLogin")
        validatePassword.checkCalledWith(userInput = "testPassword")
        interactor.checkCalledTimes(times = 1)
        interactor.checkCalledWith(login = "testLogin", password = "testPassword")
        uiUpdate.checkCalledWith(result = LoginState.Loading)

        runAsync.returnResult()
        uiUpdate.checkCalledWith(result = LoginState.Success)
    }
}

private interface FakeLoginUpdate : LoginUpdate {

    fun checkCalledWith(result: LoginState)

    class Base : FakeLoginUpdate {

        private lateinit var actualState: LoginState

        override fun checkCalledWith(result: LoginState) {
            assertEquals(result, actualState)
        }

        override fun update(state: LoginState) {
            actualState = state
        }
    }
}

private interface FakeValidateLogin : ValidateLogin {

    fun expectIncorrect()

    fun expectCorrect()

    fun checkCalledWith(userInput: String)

    class Base : FakeValidateLogin {

        private lateinit var actualLogin: String
        private var returnException: Boolean = false

        override fun expectIncorrect() {
            returnException = true
        }

        override fun expectCorrect() {
            returnException = false
        }

        override fun checkCalledWith(userInput: String) {
            assertEquals(userInput, actualLogin)
        }

        override fun validate(input: String) {
            actualLogin = input
            if (returnException)
                throw LoginInvalidException(message = "Incorrect login")
        }
    }
}

private interface FakeValidatePassword : ValidatePassword {

    fun expectIncorrect()

    fun expectCorrect()

    fun checkCalledTimes(times: Int)

    fun checkCalledWith(userInput: String)

    class Base : FakeValidatePassword {
        private lateinit var actualPassword: String
        private var returnException: Boolean = false
        private var calledCount = 0

        override fun expectIncorrect() {
            returnException = true
        }

        override fun expectCorrect() {
            returnException = false
        }

        override fun checkCalledTimes(times: Int) {
            assertEquals(times, calledCount)
        }

        override fun checkCalledWith(userInput: String) {
            assertEquals(userInput, actualPassword)
        }

        override fun validate(input: String) {
            calledCount++
            actualPassword = input
            if (returnException)
                throw PasswordInvalidException(message = "Incorrect password")
        }
    }
}

private interface FakeLoginInteractor : LoginInteractor {

    fun checkCalledWith(login: String, password: String)

    fun checkCalledTimes(times: Int)

    fun expectFailed()

    fun expectSuccess()

    class Base : FakeLoginInteractor {

        private var count = 0
        private var returnSuccess = true
        private lateinit var actualLogin: String
        private lateinit var actualPassword: String

        override fun checkCalledWith(login: String, password: String) {
            assertEquals(login, actualLogin)
            assertEquals(password, actualPassword)
        }

        override fun checkCalledTimes(times: Int) {
            assertEquals(times, count)
        }

        override fun expectFailed() {
            returnSuccess = false
        }

        override fun expectSuccess() {
            returnSuccess = true
        }

        override suspend fun login(login: String, password: String): LoginResult {
            actualLogin = login
            actualPassword = password
            count++
            return if (returnSuccess)
                LoginResult.Success
            else
                LoginResult.Failed(message = "No Internet")
        }
    }
}

private interface FakeRunAsync : RunAsync {

    fun returnResult()

    class Base : FakeRunAsync {

        private lateinit var result: Any
        private lateinit var cacheBlock: (Any) -> Unit

        override fun returnResult() {
            cacheBlock.invoke(result)
        }

        override fun <T : Any> handleAsync(
            coroutineScope: CoroutineScope,
            backgroundBlock: suspend () -> T,
            uiBlock: (T) -> Unit
        ) = runBlocking {
            result = backgroundBlock.invoke()
            cacheBlock = uiBlock as (Any) -> Unit
        }
    }
}