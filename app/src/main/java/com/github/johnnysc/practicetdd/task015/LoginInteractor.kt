package com.github.johnnysc.practicetdd.task015

/**
 * @author Demitrist on 07.01.2023
 **/

interface LoginInteractor {

    suspend fun login(): WeatherItem

}