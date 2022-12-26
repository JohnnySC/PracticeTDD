package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 1. minLength
 * 2. upperCaseLettersCount
 * 3. lowerCaseLettersCount
 * 4. numbersCount
 * 5. specialSignsCount
 *
 * @author Asatryan on 26.12.2022
 */
class ValidationTest {

    //region no arguments into constructor
    @Test
    fun test_default() {
        val validation: Validation = Validation.Password()
        val sourceList = listOf(
            "E", "A", "Z", "Q", "M",
            "w", "z", "r", "x", "t", " ",
            "!", "%", "6", "9",
            "12R", "12D3", "1A234", "B12345", "1234E56",
            "Pas", "asdE", "asVfg", "asdWfg",
            "AS", "ASD", "ASDF", "ASDFG",
            "E!@", "!R@#", "!@T#$", "!@#$%Y",
            "As", "As3", "sA$3", "As3@t", " As3@t", "As3@t ",
            "aA2aS@ s3Dd@f3%"
        )
        val actualList: List<Validation.Result> = sourceList.map {
            validation.isValid(text = it)
        }
        actualList.forEach {
            assertEquals(Validation.Result.Valid, it)
        }
    }

    @Test
    fun test_empty() {
        val validation: Validation = Validation.Password()
        val actual = validation.isValid("")
        assertEquals(Validation.Result.MinLengthInsufficient(minLength = 1), actual)
    }
    //endregion

    //region min length
    @Test
    fun test_min_length_valid() {
        val validation: Validation = Validation.Password(minLength = 2)
        val sourceList = listOf(
            "12", "123", "1234", "12345", "123456",
            "as", "asd", "asfg", "asdfg",
            "AS", "ASD", "ASDF", "ASDFG",
            "!@", "!@#", "!@#$", "!@#$%",
            "As", "As3", "sA$3", "As3@t", " As3@t", "As3@t ",
            "aA2aS@ s3Dd@f3%"
        )
        val actualList: List<Validation.Result> = sourceList.map {
            validation.isValid(it)
        }
        actualList.forEach {
            assertEquals(Validation.Result.Valid, it)
        }
    }

    @Test
    fun test_min_length_invalid() {
        val validation: Validation = Validation.Password(minLength = 2)
        val sourceList = listOf("", "a", "A", "5", "@")
        val actualList: List<Validation.Result> = sourceList.map {
            validation.isValid(it)
        }
        actualList.forEach {
            assertEquals(Validation.Result.MinLengthInsufficient(minLength = 2), it)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun test_min_length_zero() {
        Validation.Password(minLength = 0)
    }

    @Test(expected = IllegalStateException::class)
    fun test_min_length_negative() {
        Validation.Password(minLength = -1)
    }
    //endregion

    //region upper case letters count
    @Test
    fun test_upperCaseLettersCount_valid() {
        val validation: Validation = Validation.Password(upperCaseLettersCount = 1)
        val sourceList = listOf(
            "E", "A", "Z", "Q", "M",
            "12R", "12D3", "1A234", "B12345", "1234E56",
            "Pas", "asdE", "asVfg", "asdWfg",
            "AS", "ASD", "ASDF", "ASDFG",
            "E!@", "!R@#", "!@T#$", "!@#$%Y",
            "As", "As3", "sA$3", "As3@t", " As3@t", "As3@t ",
            "aA2aS@ s3Dd@f3%"
        )
        val actualList: List<Validation.Result> = sourceList.map {
            validation.isValid(it)
        }
        actualList.forEach {
            assertEquals(Validation.Result.Valid, it)
        }
    }

    @Test
    fun test_upperCaseLettersCount_invalid() {
        val validation: Validation = Validation.Password(upperCaseLettersCount = 1)
        val sourceList = listOf("m", "3", "%", " ")
        val actualList: List<Validation.Result> = sourceList.map {
            validation.isValid(it)
        }
        actualList.forEach {
            assertEquals(
                Validation.Result.UpperCaseLettersCountInsufficient(upperCaseLettersCount = 1),
                it
            )
        }
    }

    @Test(expected = IllegalStateException::class)
    fun test_upperCaseLettersCount_negative() {
        Validation.Password(upperCaseLettersCount = -1)
    }
    //endregion

    //region lower case letters count
    @Test
    fun test_lowerCaseLettersCount_valid() {
        val validation: Validation = Validation.Password(lowerCaseLettersCount = 1)
        val sourceList = listOf(
            "e", "a", "z", "q", "m",
            "12Re", "12De3", "1A23e4", "B12e345", "123e4E56",
            "Pas", "asdE", "asVfg", "asdWfg",
            "AeS", "ASeD", "ASeDF", "ASDFeG",
            "E!w@", "!R@w#", "!@T#w$", "!@w#$%Y",
            "As", "As3", "sA$3", "As3@t", " As3@t", "As3@t ",
            "aA2aS@ s3Dd@f3%"
        )
        val actualList: List<Validation.Result> = sourceList.map {
            validation.isValid(it)
        }
        actualList.forEach {
            assertEquals(Validation.Result.Valid, it)
        }
    }

    @Test
    fun test_lowerCaseLettersCount_invalid() {
        val validation: Validation = Validation.Password(lowerCaseLettersCount = 1)
        val sourceList = listOf("M", "3", "%", " ")
        val actualList: List<Validation.Result> = sourceList.map {
            validation.isValid(it)
        }
        actualList.forEach {
            assertEquals(
                Validation.Result.LowerCaseLettersCountInsufficient(lowerCaseLettersCount = 1),
                it
            )
        }
    }

    @Test(expected = IllegalStateException::class)
    fun test_lowerCaseLettersCount_negative() {
        Validation.Password(lowerCaseLettersCount = -1)
    }
    //endregion

    //region numbersCount
    @Test
    fun test_numbersCount_valid() {
        val validation: Validation = Validation.Password(numbersCount = 1)
        val sourceList = listOf(
            "1", "2", "0", "9", "3",
            "12R", "12D3", "1A234", "B12345", "1234E56",
            "P2as", "a3sdE", "asV6fg", "asdW9fg",
            "A0S", "AS9D", "ASD9F", "ASDFG5",
            "E!3@", "!R@67#", "!@T#8$", "!9@#$%Y",
            "A3s", "As3", "sA$3", "As3@t", " As3@t", "As3@t ",
            "aA2aS@ s3Dd@f3%"
        )
        val actualList: List<Validation.Result> = sourceList.map {
            validation.isValid(it)
        }
        actualList.forEach {
            assertEquals(Validation.Result.Valid, it)
        }
    }

    @Test
    fun test_numbersCount_invalid() {
        val validation: Validation = Validation.Password(numbersCount = 1)
        val sourceList = listOf("M", "m", "%", " ")
        val actualList: List<Validation.Result> = sourceList.map {
            validation.isValid(it)
        }
        actualList.forEach {
            assertEquals(
                Validation.Result.NumbersCountInsufficient(numbersCount = 1),
                it
            )
        }
    }

    @Test(expected = IllegalStateException::class)
    fun test_numbersCount_negative() {
        Validation.Password(numbersCount = -1)
    }
    //endregion

    //region specialSignsCount
    @Test
    fun test_specialSignsCount_valid() {
        val validation: Validation = Validation.Password(specialSignsCount = 1)
        val sourceList = listOf(
            "!", "@", "$", "#", "*", " ",
            "12R ", " 12D3", "1A 234", "B12 345", "123 4E56",
            "P2a s", "a3 sdE", "a sV6fg", "asdW9f g",
            "A0S ", "AS  9D", "ASD   9F", "ASDFG 5",
            "E!3@", "!R@67#", "!@T#8$", "!9@#$%Y",
            "A3 s", " As3", "sA$3", "As 3@t", " As3 @t", "A s3@t ",
            "aA2aS@ s3Dd@f3%"
        )
        val actualList: List<Validation.Result> = sourceList.map {
            validation.isValid(it)
        }
        actualList.forEach {
            assertEquals(Validation.Result.Valid, it)
        }
    }

    @Test
    fun test_specialSignsCount_invalid() {
        val validation: Validation = Validation.Password(specialSignsCount = 1)
        val sourceList = listOf("M", "m", "5")
        val actualList: List<Validation.Result> = sourceList.map {
            validation.isValid(it)
        }
        actualList.forEach {
            assertEquals(
                Validation.Result.SpecialSignsInsufficient(specialSignsCount = 1),
                it
            )
        }
    }

    @Test(expected = IllegalStateException::class)
    fun test_specialSignsCount_negative() {
        Validation.Password(specialSignsCount = -1)
    }
    //endregion
}