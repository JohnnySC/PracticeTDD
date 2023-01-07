package com.github.johnnysc.practicetdd.task015

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * @author Demitrist on 07.01.2023
 **/

interface LoginCommunication {

    fun map(source: LoginState)
    fun observe(owner: LifecycleOwner, observer: Observer<LoginState>)

}