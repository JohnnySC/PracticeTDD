package com.github.johnnysc.practicetdd.task015

/**
 * @author Demitrist on 07.01.2023
 **/

interface LoginState {

    data class Success(private val value: WeatherUiModel) : LoginState
    data class Error(private val value: WeatherUiModel) : LoginState
    data class EmailError(private val value: String) : LoginState
    data class PasswordError(private val value: String) : LoginState
    data class TwoErrors(
        private val loginError: String,
        private val passwordError: String,
    ) : LoginState
}