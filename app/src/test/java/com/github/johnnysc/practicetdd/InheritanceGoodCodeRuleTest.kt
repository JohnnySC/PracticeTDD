package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 23.01.2023
 */
class InheritanceGoodCodeRuleTest {

    @Test
    fun `test valid good code`() {
        val rule: GoodCodeRule = GoodCodeRule.Inheritance()
        val sourceList = listOf(
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "interface DataSource {\n" +
                    "    fun fetch() : Any\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "interface MutableDataSource : DataSource {\n" +
                    "    fun save(data: Any)\n" +
                    "}\n",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "interface MutableDataSource : DataSource, Serializable {\n" +
                    "    fun save(data: Any)\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "\n" +
                    "abstract class AbstractX\n",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class AbstractX : DataSource",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class AbstractX : DataSource, Serializable",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class Phone : AbstractX() {\n" +
                    "    \n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class Phone : AbstractX(), DataSource {\n" +
                    "    \n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class Phone : AbstractX(), DataSource, Serializable {\n" +
                    "    \n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class Phone : DataSource {\n" +
                    "    \n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class Phone : DataSource, Serializable {\n" +
                    "    \n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class BackPressAbstractActivity : BaseActivity()",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class BackPressAbstractFragment : BaseFragment()"
        )

        sourceList.forEach {
            assertEquals(true, rule.isValid(text = it))
        }
    }

    @Test
    fun `test invalid good code`() {
        val rule: GoodCodeRule = GoodCodeRule.Inheritance()
        val sourceList = listOf(
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "class Phone {\n" +
                    "    fun fetch() {\n" +
                    "        \n" +
                    "    }\n" +
                    "}",
            "package com.github.johnnysc.practicetdd\n" +
                    "\n" +
                    "abstract class AbstractX : MainLevelAbstract()"
        )
        sourceList.forEach {
            assertEquals(false, rule.isValid(text = it))
        }
    }
}