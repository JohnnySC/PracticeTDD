package com.github.johnnysc.practicetdd

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.github.johnnysc.practicetdd.task015.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 27.12.2022
 */
class LoginViewModelTest {

    @Test
    fun `test email and password incorrect`() {
        val communication = FakeLoginCommunication.Base()
        val interactor = FakeInteractor(WeatherItem.Error(ExceptionType.NETWORK_UNAVAILABLE))
        val email = FakeValidator(false, "Email is empty")
        val password = FakeValidator(false, "Password is empty")
        val viewModel = LoginViewModel(
            communication = communication,
            interactor = interactor,
            mapper = FakeMapper(""),
            validateEmail = email,
            validatePassword = password,
            dispatchers = TestDispatchersList()
        )
        viewModel.login(email = "", password = "")
        assertEquals(
            LoginState.TwoErrors(
                loginError = "Email is empty",
                passwordError = "Password is empty"
            ), communication.state()
        )
    }

    @Test
    fun `test only email incorrect`() {
        val communication = FakeLoginCommunication.Base()
        val interactor = FakeInteractor(WeatherItem.Error(ExceptionType.NETWORK_UNAVAILABLE))
        val email = FakeValidator(false, "Input is empty")
        val password = FakeValidator(true, "no matter what")
        val viewModel = LoginViewModel(
            communication,
            interactor,
            FakeMapper(""),
            email,
            password,
            TestDispatchersList()
        )
        viewModel.login(email = "", password = "asdfASDF123")
        assertEquals(
            LoginState.EmailError(
                value = "Input is empty"
            ),
            communication.state()
        )
    }

    @Test
    fun `test only password incorrect`() {
        val communication = FakeLoginCommunication.Base()
        val interactor = FakeInteractor(WeatherItem.Error(ExceptionType.NETWORK_UNAVAILABLE))
        val password = FakeValidator(false, "Input is empty")
        val email = FakeValidator(true, "no matter what")
        val viewModel = LoginViewModel(
            communication,
            interactor,
            FakeMapper(""),
            email,
            password,
            TestDispatchersList()
        )
        viewModel.login(email = "asdf@gmail.com", password = "")
        assertEquals(
            LoginState.PasswordError(
                value = "Input is empty"
            ),
            communication.state()
        )
    }

    @Test
    fun `test input valid interactor failed`() {
        val communication = FakeLoginCommunication.Base()
        val interactor =
            FakeInteractor(WeatherItem.Error(exceptionType = ExceptionType.NETWORK_UNAVAILABLE))
        val valid = FakeValidator(true, "no matter")
        val viewModel = LoginViewModel(
            communication,
            interactor,
            FakeMapper("Network error: "),
            valid,
            valid,
            TestDispatchersList()
        )
        viewModel.login(email = "johnnysc091@gmail.com", password = "asdfASDF123")
        assertEquals(
            communication.state(),
            LoginState.Error(value = WeatherUiModel("Network error: NETWORK_UNAVAILABLE", true))
        )
    }

    @Test
    fun `test valid input and successful response`() {
        val communication = FakeLoginCommunication.Base()
        val interactor = FakeInteractor(
            WeatherItem.Basic(
                description = "sunny",
                temp = 20,
                feelsLike = 22
            )
        )
        val valid = FakeValidator(true, "no matter what")
        val viewModel = LoginViewModel(
            communication,
            interactor,
            FakeMapper("don't use error here"),
            valid,
            valid,
            TestDispatchersList()
        )
        viewModel.login(email = "johnnysc091@gmail.com", password = "asdfASDF123")
        assertEquals(
            communication.state(),
            LoginState.Success(value = WeatherUiModel(description = "sunny 20 22"))
        )
    }
}

private class TestDispatchersList(
    private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()
) : DispatchersList {

    override fun io(): CoroutineDispatcher = dispatcher
    override fun ui(): CoroutineDispatcher = dispatcher
}

private class FakeMapper(private val error: String) : WeatherUiMapper<WeatherUiModel> {
    override fun map(feelsLike: Int, description: String, temp: Int): WeatherUiModel {
        return WeatherUiModel(description = "$description $temp $feelsLike")
    }

    override fun map(exceptionType: ExceptionType): WeatherUiModel {
        return WeatherUiModel(description = error + exceptionType.toString(), isError = true)
    }
}

private class FakeValidator(private val valid: Boolean, private val message: String) :
    UiValidator {
    override fun errorMessage(): String {
        return message
    }

    override fun isValid(text: String): Boolean {
        return valid
    }
}

private interface FakeLoginCommunication : LoginCommunication {

    fun state(): LoginState

    class Base : FakeLoginCommunication {
        private var state: LoginState? = null

        override fun state(): LoginState = state!!

        override fun map(source: LoginState) {
            state = source
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<LoginState>) = Unit
    }
}

private class FakeInteractor(
    private val item: WeatherItem
) : LoginInteractor {

    override suspend fun login(): WeatherItem {
        return item
    }
}