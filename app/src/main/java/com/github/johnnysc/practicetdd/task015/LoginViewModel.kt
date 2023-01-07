package com.github.johnnysc.practicetdd.task015

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @author Demitrist on 07.01.2023
 **/

class LoginViewModel(
    private val communication: LoginCommunication,
    private val interactor: LoginInteractor,
    private val mapper: WeatherUiMapper<WeatherUiModel>,
    private val validateEmail: UiValidator,
    private val validatePassword: UiValidator,
    private val dispatchers: DispatchersList,
) {

    fun login(email: String, password: String) {
        val correctEmail = validateEmail.isValid(email)
        val correctPassword = validatePassword.isValid(password)

        if (!correctEmail || !correctPassword){
            if (!correctEmail && correctPassword)
                communication.map(LoginState.EmailError(validateEmail.errorMessage()))
            else if (correctEmail && !correctPassword)
                communication.map(LoginState.PasswordError(validatePassword.errorMessage()))
            else
                communication.map(LoginState.TwoErrors(
                    validateEmail.errorMessage(),
                    validatePassword.errorMessage()))
            return
        }

        CoroutineScope(dispatchers.io()).launch {
            val interactorResponse = interactor.login()
            if (interactorResponse.isError())
                communication.map(LoginState.Error(interactorResponse.map(mapper)))
            else
                communication.map(LoginState.Success(interactorResponse.map(mapper)))
        }
    }
}