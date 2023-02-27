package com.github.johnnysc.practicetdd

import kotlin.math.max

interface MyStack<T> {

    fun pop(): T

    fun push(item: T)

    class LIFO<T : Any?>(private val maxCount: Int) : MyStack<T> {

        private val array:Array<Any?>
        private var position = -1

        init {
            if (maxCount < 1) {
                throw java.lang.IllegalStateException("max count should be positive!")
            }
            array = Array(maxCount) { null }
        }


        override fun pop(): T {
            if (position == -1) {
                throw java.lang.IllegalStateException("array is empty")
            }

            val item = array[position] as T
            array[position] = null
            position--
            return item
        }

        override fun push(item: T) {
            if (position + 1 == maxCount) {
                throw java.lang.IllegalStateException("Stack overflow exception, maximum is $maxCount")
            }
            array[++position] = item
        }

    }

    class FIFO<T>(private val maxCount: Int) : MyStack<T> {

        private val array:Array<Any?>
        private var position = -1

        init {
            if (maxCount < 1) {
                throw java.lang.IllegalStateException("max count should be positive!")
            }
            array = Array(maxCount) { null }

        }


        override fun pop(): T {
            if (position == -1) {
                throw java.lang.IllegalStateException("array is empty")
            }

            val item = array[position] as T
            array[position] = null
            position--
            return item
        }

        override fun push(item: T) {
            if (position + 1 == maxCount) {
                throw java.lang.IllegalStateException("Stack overflow exception, maximum is $maxCount")
            }
            position++
            if (position == 0) {
                array[position] = item
            } else {
                for (i in position downTo 1) {
                    array[position] = array[position - 1]
                }
                array[0] = item
            }

        }
    }

}