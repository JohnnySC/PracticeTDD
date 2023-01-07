package com.github.johnnysc.practicetdd.task015

import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author Demitrist on 07.01.2023
 **/

interface DispatchersList {

    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher

}