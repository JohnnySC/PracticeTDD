package com.github.johnnysc.practicetdd.task015

/**
 * @author Demitrist on 07.01.2023
 **/

data class WeatherUiModel(
    private val description: String,
    private val isError: Boolean = false,
)