package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 15.01.2023
 */
class EncapsulationRuleTest {

    @Test
    fun `encapsulation rule passed`() {
        val rule: GoodCodeRule = GoodCodeRule.Encapsulation()
        val textList = listOf(
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class Repository(private val dataSource: DataSource) {\n" +
                    "\n" +
                    "    private var page: Int = 0\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class Repository(private val dataSource: DataSource) {\n" +
                    "\n" +
                    "    protected var page: Int = 0\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class Repository(protected val dataSource: DataSource) {\n" +
                    "\n" +
                    "    private var page: Int = 0\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class Repository(protected val dataSource: DataSource) {\n" +
                    "\n" +
                    "    protected var page: Int = 0\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class Repository(protected val dataSource: DataSource) {\n" +
                    "\n" +
                    "    protected abstract val page: Int\n" +
                    "}",
        )
        textList.forEach { text ->
            val actual = rule.isValid(text = text)
            val expected = true
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `encapsulation rule not passed`() {
        val rule: GoodCodeRule = GoodCodeRule.Encapsulation()
        val textList = listOf(
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class Repository(val dataSource: DataSource) {\n" +
                    "\n" +
                    "    var page: Int = 0\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class Repository(private val dataSource: DataSource) {\n" +
                    "\n" +
                    "    var page: Int = 0\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class Repository(val dataSource: DataSource) {\n" +
                    "\n" +
                    "   private var page: Int = 0\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class Repository(protected val dataSource: DataSource) {\n" +
                    "\n" +
                    "    var page: Int = 0\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class Repository(val dataSource: DataSource) {\n" +
                    "\n" +
                    "   protected var page: Int = 0\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class Repository(val dataSource: DataSource) {\n" +
                    "\n" +
                    "   protected abstract val page: Int\n" +
                    "}",
        )
        textList.forEach { text ->
            val actual = rule.isValid(text = text)
            val expected = false
            assertEquals(expected, actual)
        }
    }
}