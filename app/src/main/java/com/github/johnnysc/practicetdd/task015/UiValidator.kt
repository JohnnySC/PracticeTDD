package com.github.johnnysc.practicetdd.task015

/**
 * @author Demitrist on 07.01.2023
 **/

interface UiValidator {
    fun isValid(text:String):Boolean
    fun errorMessage():String
}