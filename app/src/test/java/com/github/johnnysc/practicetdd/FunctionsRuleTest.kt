package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 30.01.2023
 */
class FunctionsRuleTest {

    @Test
    fun test_function_in_interface_valid() {
        val goodCodeRule: GoodCodeRule = GoodCodeRule.Functions()
        val sourceList = listOf(
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "interface DataSource {\n" +
                    "    \n" +
                    "    fun fetch(): String\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "interface MutableDataSource {\n" +
                    "\n" +
                    "    fun fetch(): String\n" +
                    "\n" +
                    "    fun save(data: String)\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "interface Repository {\n" +
                    "\n" +
                    "    fun fetchString(): String\n" +
                    "    fun fetchInt(): Int\n" +
                    "    fun saveString(data: String)\n" +
                    "    fun saveInt(data: Int)\n" +
                    "    fun fetchDouble(): Double\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "interface Repository {\n" +
                    "\n" +
                    "    fun fetchCustomObject(\n" +
                    "        name: String,\n" +
                    "        data: Long,\n" +
                    "        id: Int,\n" +
                    "        bytes: ByteArray,\n" +
                    "        dataSource: DataSource,\n" +
                    "    ): String\n" +
                    "}\n"
        )
        sourceList.forEach {
            assertEquals(true, goodCodeRule.isValid(text = it))
        }
    }

    @Test
    fun test_function_in_interface_invalid() {
        val goodCodeRule: GoodCodeRule = GoodCodeRule.Functions()
        val sourceList = listOf(
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "interface DataSource {\n" +
                    "\n" +
                    "    fun save(data: String) = Unit\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "interface DelayResponse {\n" +
                    "\n" +
                    "    fun delayAfter(millis: Int)\n" +
                    "\n" +
                    "    fun handle() = delayAfter(0)\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "interface Repository {\n" +
                    "\n" +
                    "    fun fetchString(): String\n" +
                    "    fun fetchInt(): Int\n" +
                    "    fun saveString(data: String)\n" +
                    "    fun saveInt(data: Int)\n" +
                    "    fun fetchDouble(): Double\n" +
                    "    fun saveDouble(data: Double)\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "interface Repository {\n" +
                    "\n" +
                    "    fun fetchCustomObject(\n" +
                    "        name: String,\n" +
                    "        data: Long,\n" +
                    "        id: Int,\n" +
                    "        bytes: ByteArray,\n" +
                    "        dataSource: DataSource,\n" +
                    "        arg: Any\n" +
                    "    ): String\n" +
                    "}\n"
        )
        sourceList.forEach {
            assertEquals(false, goodCodeRule.isValid(text = it))
        }
    }

    @Test
    fun test_function_in_class_valid() {
        val goodCodeRule: GoodCodeRule = GoodCodeRule.Functions()
        val sourceList = listOf(
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class A : X {\n" +
                    "    \n" +
                    "    override fun x() {\n" +
                    "        println(\"hello\")\n" +
                    "    }\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class A(\n" +
                    "    private val logging: Logging\n" +
                    ") {\n" +
                    "\n" +
                    "    protected fun handle(text: String) {\n" +
                    "        logging.log(\"tag\", text)\n" +
                    "    }\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class A {\n" +
                    "\n" +
                    "    protected abstract fun handleSuccess(text: Char)\n" +
                    "}"
        )
        sourceList.forEach {
            assertEquals(true, goodCodeRule.isValid(text = it))
        }
    }

    @Test
    fun test_function_in_class_invalid() {
        val goodCodeRule: GoodCodeRule = GoodCodeRule.Functions()
        val sourceList = listOf(
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class A {\n" +
                    "\n" +
                    "    fun x() {\n" +
                    "        println(\"hello\")\n" +
                    "    }\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class A {\n" +
                    "\n" +
                    "    protected fun y() {\n" +
                    "        println(\"hello\")\n" +
                    "    }\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class A {\n" +
                    "\n" +
                    "    private fun y() {\n" +
                    "        println(\"hello\")\n" +
                    "    }\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class A {\n" +
                    "\n" +
                    "    abstract fun handleSuccess(text: Char)\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class A {\n" +
                    "\n" +
                    "    private fun handleSuccess(text: Char) {\n" +
                    "        \n" +
                    "    }\n" +
                    "}"
        )
        sourceList.forEach {
            assertEquals(false, goodCodeRule.isValid(text = it))
        }
    }
}