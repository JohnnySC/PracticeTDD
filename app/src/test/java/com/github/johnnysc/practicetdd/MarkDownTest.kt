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
                listOf("with")
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things**",
                listOf("with")
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with** markdown things",
                listOf("here is some text ")
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with** markdown things",
                listOf("with")
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
                listOf("with", " markdown ")
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things",
                listOf("text ", "with")
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things",
                listOf("here is")
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
                listOf("with")
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things*",
                listOf("with")
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with* markdown things",
                listOf("here is some text ")
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with* markdown things",
                listOf("with")
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
                listOf("with", " markdown ")
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things",
                listOf("text ", "with")
            ),
            MarkDown.ResultItem.Base(
                "someColor",
                "here is some text with markdown things",
                listOf("here is")
            )
        )
        sourceList.forEachIndexed { index, source ->
            val actual = markDownParser.parse(source)
            assertEquals(expectedList[index], actual)
        }
    }
    //endregion
}