package com.github.johnnysc.practicetdd

interface MyStack<T> {

    fun pop(): T
    fun push(item: T)

    class LIFO<T : Any?>(private val maxCount: Int) : MyStack<T> {

        private val array: Array<Any?>
        private var position: Int = 0

        init {
            if (maxCount <= 0)
                throw IllegalStateException("max count must be positive!")
            array = Array(maxCount) { null }
        }

        @Suppress("UNCHECKED_CAST")
        override fun pop(): T {
            if (position == 0)
                throw IllegalStateException("Array is empty!")
            else {
                val item = array[position - 1] as T
                array[position-- - 1] = null
                return item
            }
        }

        override fun push(item: T) {
            if (position == maxCount) {
                throw IllegalStateException("Stack overflow exception, maximum is $maxCount")
            }
            array[position++] = item
        }

    }


    class FIFO<T>(private val maxCount: Int) : MyStack<T> {
        override fun pop(): T {
            TODO("Not yet implemented")
        }

        override fun push(item: T) {
            TODO("Not yet implemented")
        }

    }

}