package com.github.johnnysc.practicetdd

/**
 * @author Asatryan on 26.12.2022
 */
interface For {

    fun repeat(max: Int, block: (Int) -> Boolean) =
        repeat(max, 0, block = block)

    fun repeat(max: Int, start: Int, block: (Int) -> Boolean)

    class Base : For {

        override fun repeat(max: Int, start: Int, block: (Int) -> Boolean) {
            for (i in start until max) {
                if (block.invoke(i))
                    break
            }
        }
    }
}