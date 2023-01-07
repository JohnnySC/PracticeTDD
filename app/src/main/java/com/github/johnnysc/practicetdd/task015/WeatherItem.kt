package com.github.johnnysc.practicetdd.task015

/**
 * @author Demitrist on 07.01.2023
 **/

interface WeatherItem {

    fun isError(): Boolean
    fun map(mapper: WeatherUiMapper<WeatherUiModel>): WeatherUiModel

    class Basic(
        private val description: String,
        private val feelsLike: Int,
        private val temp: Int,
    ) : WeatherItem {

        override fun isError(): Boolean = false
        override fun map(mapper: WeatherUiMapper<WeatherUiModel>): WeatherUiModel {
            return mapper.map(feelsLike = feelsLike, description = description, temp = temp)
        }
    }

    class Error(private val exceptionType: ExceptionType) : WeatherItem {

        override fun isError(): Boolean = true
        override fun map(mapper: WeatherUiMapper<WeatherUiModel>): WeatherUiModel {
            return mapper.map(exceptionType = exceptionType)
        }
    }
}