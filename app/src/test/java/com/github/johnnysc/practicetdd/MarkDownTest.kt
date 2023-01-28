package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 28.01.2023
 */
class MarkDownTest {

    //region main
    @Test
    fun test_no_parts_not_contains_2_signs() {
        val markDownParser = MarkDown.Parser.Base("someColor", "**")
        val actual = markDownParser.parse("here is some text without markdown things")
        val expected = MarkDown.ResultItem.Base(
            "someColor",
            "here is some text without markdown things",
            emptyList()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun test_no_parts_one_contains_2_signs() {
        val markDownParser = MarkDown.Parser.Base("someColor", "**")
        val sourceList = listOf(
            "here is some text without** markdown things",
            "here is some text without markdown things**",
            "**here is some text without markdown things"
        )
        sourceList.forEach {
            val actual = markDownParser.parse(it)
            val expected = MarkDown.ResultItem.Base("someColor", it, emptyList())
            assertEquals(expected, actual)
        }
    }

    @Test
    fun test_one_part_2_signs() {
        val markDownParser = MarkDown.Parser.Base("someColor", "**")
        val sourceList = listOf(
            "here is some text **with** markdown things",
            "here is some text **with** markdown things**",
            "**here is some text **with** markdown things",
            "here is some text **with**** markdown things"
        )
        val expectedList = listOf(
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things",
                listOf(MarkDown.ResultItem.StringAndIndex(string = "with", index = 18))
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things**",
                listOf(MarkDown.ResultItem.StringAndIndex("with", 18))
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with** markdown things",
                listOf(MarkDown.ResultItem.StringAndIndex("here is some text ", 0))
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with** markdown things",
                listOf(MarkDown.ResultItem.StringAndIndex("with", 18))
            ),
        )

        sourceList.forEachIndexed { index, source ->
            val actual = markDownParser.parse(source)
            assertEquals(expectedList[index], actual)
        }
    }

    @Test
    fun test_two_parts_2_signs() {
        val markDownParser = MarkDown.Parser.Base("someColor", "**")
        val sourceList = listOf(
            "here is some text **with**** markdown **things",
            "here is some **text ****with** markdown things",
            "******here is** some text with markdown things"
        )
        val expectedList = listOf(
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things",
                listOf(
                    MarkDown.ResultItem.StringAndIndex("with", 18),
                    MarkDown.ResultItem.StringAndIndex(
                        " markdown ", 22
                    )
                )
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things",
                listOf(
                    MarkDown.ResultItem.StringAndIndex("text ", 13),
                    MarkDown.ResultItem.StringAndIndex("with", 18)
                )
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things",
                listOf(MarkDown.ResultItem.StringAndIndex("here is", 0))
            )
        )
        sourceList.forEachIndexed { index, source ->
            val actual = markDownParser.parse(source)
            assertEquals(expectedList[index], actual)
        }
    }
    //endregion

    //region one sign
    @Test
    fun test_no_parts_not_contains() {
        val parser = MarkDown.Parser.OneSignDelimiter("someColor", '*')
        val actual = parser.parse("here is some text without markdown things")
        val expected = MarkDown.ResultItem.Base(
            "someColor",
            "here is some text without markdown things",
            emptyList()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun test_no_parts_one_contains() {
        val parser = MarkDown.Parser.OneSignDelimiter("someColor", '*')
        val sourceList = listOf(
            "here is some text without* markdown things",
            "here is some text without markdown things*",
            "*here is some text without markdown things",
        )
        sourceList.forEach {
            val actual = parser.parse(it)
            val expected = MarkDown.ResultItem.Base("someColor", it, emptyList())
            assertEquals(expected, actual)
        }
    }

    @Test
    fun test_one_part() {
        val markDownParser = MarkDown.Parser.OneSignDelimiter("someColor", '*')
        val sourceList = listOf(
            "here is some text *with* markdown things",
            "here is some text *with* markdown things*",
            "*here is some text *with* markdown things",
            "here is some text *with** markdown things"
        )
        val expectedList = listOf(
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things",
                listOf(MarkDown.ResultItem.StringAndIndex("with", 18))
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things*",
                listOf(MarkDown.ResultItem.StringAndIndex("with", 18))
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with* markdown things",
                listOf(MarkDown.ResultItem.StringAndIndex("here is some text ", 0))
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with* markdown things",
                listOf(MarkDown.ResultItem.StringAndIndex("with", 18))
            ),
        )

        sourceList.forEachIndexed { index, source ->
            val actual = markDownParser.parse(source)
            assertEquals(expectedList[index], actual)
        }
    }

    @Test
    fun test_two_parts_2() {
        val markDownParser = MarkDown.Parser.OneSignDelimiter("someColor", '*')
        val sourceList = listOf(
            "here is some text *with** markdown *things",
            "here is some *text **with* markdown things",
            "***here is* some text with markdown things"
        )
        val expectedList = listOf(
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things",
                listOf(
                    MarkDown.ResultItem.StringAndIndex("with", 18),
                    MarkDown.ResultItem.StringAndIndex(" markdown ", 22)
                )
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things",
                listOf(
                    MarkDown.ResultItem.StringAndIndex("text ", 13),
                    MarkDown.ResultItem.StringAndIndex("with", 18)
                )
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things",
                listOf(MarkDown.ResultItem.StringAndIndex("here is", 0))
            )
        )
        sourceList.forEachIndexed { index, source ->
            val actual = markDownParser.parse(source)
            assertEquals(expectedList[index], actual)
        }
    }
    //endregion

    @Test
    fun test_2_matches() {
        val markDownParser = MarkDown.Parser.OneSignDelimiter("someColor", '*')
        val sourceList = listOf(
            "here is RED some text *RED* markdown things",
            "here is *RED* some text *RED* markdown things",
        )
        val expectedList = listOf(
            MarkDown.ResultItem.Base(
                "someColor",
                "here is RED some text RED markdown things",
                listOf(
                    MarkDown.ResultItem.StringAndIndex("RED", 22)
                )
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is RED some text RED markdown things",
                listOf(
                    MarkDown.ResultItem.StringAndIndex("RED", 8),
                    MarkDown.ResultItem.StringAndIndex("RED", 22)
                )
            )
        )
        sourceList.forEachIndexed { index, source ->
            val actual = markDownParser.parse(source)
            assertEquals(expectedList[index], actual)
        }
    }

    @Test
    fun test_2_matches_2_signs() {
        val markDownParser = MarkDown.Parser.Base("someColor", "**")
        val sourceList = listOf(
            "here is RED some text **RED** markdown things",
            "here is **RED** some text **RED** markdown things",
        )
        val expectedList = listOf(
            MarkDown.ResultItem.Base(
                "someColor",
                "here is RED some text RED markdown things",
                listOf(
                    MarkDown.ResultItem.StringAndIndex("RED", 22)
                )
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is RED some text RED markdown things",
                listOf(
                    MarkDown.ResultItem.StringAndIndex("RED", 8),
                    MarkDown.ResultItem.StringAndIndex("RED", 22)
                )
            )
        )
        sourceList.forEachIndexed { index, source ->
            val actual = markDownParser.parse(source)
            assertEquals(expectedList[index], actual)
        }
    }
}