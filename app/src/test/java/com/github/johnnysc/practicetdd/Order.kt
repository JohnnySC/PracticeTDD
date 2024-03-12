package com.github.johnnysc.practicetdd

data class Order(private val list: MutableList<String>) {

    fun add(name: String) {
        list.add(name)
    }
}