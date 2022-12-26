package com.github.johnnysc.practicetdd

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 26.12.2022
 */
class LotteryTest {

    @Test
    fun test_not_success() {
        val list = listOf<Int>(
            10,
            12,
            13,
            14,
            20,
            21,
            23,
            24,
            30,
            31,
            32,
            34,
            35,
            1000,
            1002,
            1003,
            1004,
            1005,
            1006,
            1007,
            1008,
            1009,
            1011,
            100_000,
            100_002,
            110_111,
            1000_0000,
            1000_0002,
            1000_0003,
            1234_5678,
            1111_1112,
            1009_1000
        )
        val factory = TicketFactory.Base()
        val expected = false
        list.forEach { number ->
            val ticket: LotteryTicket = factory.ticket(number = number)
            assertEquals(false, ticket.isFake())
            val actual = ticket.isWinner()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun test_success() {
        val list = listOf(
            11,
            22,
            33,
            44,
            55,
            66,
            77,
            88,
            99,
            1001,
            1010,
            1111,
            1212,
            1221,
            2112,
            2222,
            2002,
            2020,
            100_001,
            101_002,
            111_111,
            112_121,
            110_002,
            1000_0001,
            1000_0010,
            1000_0100,
            1000_1000,
            1234_0307,
            1234_0208,
            1234_0217,
            9999_9999,
            4444_0907,
            4444_5551,
        )
        val factory = TicketFactory.Base()
        val expected = true
        list.forEach { number ->
            val ticket: LotteryTicket = factory.ticket(number = number)
            assertEquals(false, ticket.isFake())
            val actual = ticket.isWinner()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun test_fake() {
        val factory = TicketFactory.Base()
        val list = listOf(
            0,
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8,
            9,
            -1,
            -2,
            -3,
            -4,
            -5,
            -6,
            -7,
            -8,
            -9,
            -10,
            -12,
            -123,
            -2345,
            Int.MIN_VALUE,
            Int.MAX_VALUE,
            1234567890,
            123,
            12345,
            1234567,
            123456789
        )
        list.forEach { number ->
            val actual: LotteryTicket = factory.ticket(number = number)
            assertEquals(true, actual.isFake())
        }
    }
}