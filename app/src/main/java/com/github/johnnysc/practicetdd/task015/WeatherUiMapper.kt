package com.github.johnnysc.practicetdd.task015

/**
 * @author Demitrist on 07.01.2023
 **/

interface WeatherUiMapper<T : WeatherUiModel> : ErrorMapper<T>, SuccessMapper<T>

interface ErrorMapper<T : WeatherUiModel> {
    fun map(exceptionType: ExceptionType): T
}

interface SuccessMapper<T : WeatherUiModel> {
    fun map(feelsLike: Int, description: String, temp: Int): T
}