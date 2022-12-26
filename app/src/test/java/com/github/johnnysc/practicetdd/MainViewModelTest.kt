package com.github.johnnysc.practicetdd

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Asatryan on 26.12.2022
 */
class MainViewModelTest {

    @Test
    fun test_choose_2_different_filters() {
        val filters = mutableListOf<GoodFilter>(
            TestRamFilter(ram = 16),
            TestRamFilter(ram = 8),
            TestOsFilter(OS.MAC),
            TestOsFilter(OS.WINDOWS),
            TestOsFilter(OS.LINUX),
            TestPriceUnder(1600.0)
        )
        val products = mutableListOf<Good>(
            TestGood(8, OS.WINDOWS, 14.0, ProcessorType.INTEL, 1400.0),
            TestGood(16, OS.WINDOWS, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(8, OS.LINUX, 12.0, ProcessorType.AMD, 1400.0),
            TestGood(16, OS.LINUX, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(8, OS.MAC, 13.0, ProcessorType.M1, 2000.0),
            TestGood(16, OS.MAC, 13.0, ProcessorType.M1, 2500.0),
        )
        val communication = TestCommunication()
        val viewModel = MainViewModel(
            filters,
            products,
            communication,
            TestFiltersCommunication()
        )
        viewModel.change(TestRamFilter(ram = 16))
        var actual = communication.list
        var expected = listOf<Good>(
            TestGood(16, OS.WINDOWS, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(16, OS.LINUX, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(16, OS.MAC, 13.0, ProcessorType.M1, 2500.0),
        )
        assertEquals(expected, actual)

        viewModel.change(TestOsFilter(OS.MAC))
        actual = communication.list
        expected = listOf(
            TestGood(16, OS.MAC, 13.0, ProcessorType.M1, 2500.0),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun test_add_and_remove_filters() {
        val filters = mutableListOf<GoodFilter>(
            TestRamFilter(ram = 16),
            TestRamFilter(ram = 8),
            TestOsFilter(OS.MAC),
            TestOsFilter(OS.WINDOWS),
            TestOsFilter(OS.LINUX),
            TestPriceUnder(1600.0)
        )
        val products = mutableListOf<Good>(
            TestGood(8, OS.WINDOWS, 14.0, ProcessorType.INTEL, 1400.0),
            TestGood(16, OS.WINDOWS, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(8, OS.LINUX, 12.0, ProcessorType.AMD, 1400.0),
            TestGood(16, OS.LINUX, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(8, OS.MAC, 13.0, ProcessorType.M1, 2000.0),
            TestGood(16, OS.MAC, 13.0, ProcessorType.M1, 2500.0),
        )
        val communication = TestCommunication()
        val viewModel = MainViewModel(
            filters,
            products,
            communication,
            TestFiltersCommunication()
        )
        viewModel.change(TestRamFilter(ram = 16))
        var actual = communication.list
        var expected = listOf<Good>(
            TestGood(16, OS.WINDOWS, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(16, OS.LINUX, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(16, OS.MAC, 13.0, ProcessorType.M1, 2500.0),
        )
        assertEquals(expected, actual)
        viewModel.change(TestRamFilter(ram = 16))

        actual = communication.list
        expected = listOf<Good>(
            TestGood(8, OS.WINDOWS, 14.0, ProcessorType.INTEL, 1400.0),
            TestGood(16, OS.WINDOWS, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(8, OS.LINUX, 12.0, ProcessorType.AMD, 1400.0),
            TestGood(16, OS.LINUX, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(8, OS.MAC, 13.0, ProcessorType.M1, 2000.0),
            TestGood(16, OS.MAC, 13.0, ProcessorType.M1, 2500.0),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun test_add_2_then_remove_first() {
        val filters = mutableListOf<GoodFilter>(
            TestRamFilter(ram = 16),
            TestRamFilter(ram = 8),
            TestOsFilter(OS.MAC),
            TestOsFilter(OS.WINDOWS),
            TestOsFilter(OS.LINUX),
            TestPriceUnder(1600.0)
        )
        val products = mutableListOf<Good>(
            TestGood(8, OS.WINDOWS, 14.0, ProcessorType.INTEL, 1400.0),
            TestGood(16, OS.WINDOWS, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(8, OS.LINUX, 12.0, ProcessorType.AMD, 1400.0),
            TestGood(16, OS.LINUX, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(8, OS.MAC, 13.0, ProcessorType.M1, 2000.0),
            TestGood(16, OS.MAC, 13.0, ProcessorType.M1, 2500.0),
        )
        val communication = TestCommunication()
        val viewModel = MainViewModel(
            filters,
            products,
            communication,
            TestFiltersCommunication()
        )
        viewModel.change(TestRamFilter(ram = 16))
        var actual = communication.list
        var expected = listOf<Good>(
            TestGood(16, OS.WINDOWS, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(16, OS.LINUX, 16.0, ProcessorType.INTEL, 1600.0),
            TestGood(16, OS.MAC, 13.0, ProcessorType.M1, 2500.0),
        )
        assertEquals(expected, actual)

        viewModel.change(TestOsFilter(OS.MAC))
        actual = communication.list
        expected = listOf(
            TestGood(16, OS.MAC, 13.0, ProcessorType.M1, 2500.0),
        )
        assertEquals(expected, actual)

        viewModel.change(TestRamFilter(ram = 16))
        actual = communication.list
        expected = listOf(
            TestGood(8, OS.MAC, 13.0, ProcessorType.M1, 2000.0),
            TestGood(16, OS.MAC, 13.0, ProcessorType.M1, 2500.0),
        )
        assertEquals(expected, actual)
    }

    private class TestCommunication : Communication<List<Good>> {

        val list = mutableListOf<Good>()

        override fun observe(owner: LifecycleOwner, observer: Observer<List<Good>>) = Unit

        override fun map(source: List<Good>) {
            list.clear()
            list.addAll(source)
        }
    }

    private class TestFiltersCommunication : Communication<List<GoodFilter>> {
        val list = mutableListOf<GoodFilter>()

        override fun map(source: List<GoodFilter>) {
            list.clear()
            list.addAll(source)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<List<GoodFilter>>) = Unit
    }

    private data class TestOsFilter(private val os: OS) : GoodFilter.Abstract() {

        override fun map(
            ram: Int,
            os: OS,
            displaySize: Double,
            processor: ProcessorType,
            price: Double
        ): Boolean = this.os == os
    }

    private data class TestRamFilter(private val ram: Int) : GoodFilter.Abstract() {

        override fun map(
            ram: Int,
            os: OS,
            displaySize: Double,
            processor: ProcessorType,
            price: Double
        ): Boolean = this.ram == ram
    }

    private data class TestPriceUnder(private val maxPrice: Double) : GoodFilter.Abstract() {

        override fun map(
            ram: Int,
            os: OS,
            displaySize: Double,
            processor: ProcessorType,
            price: Double
        ): Boolean = this.maxPrice >= price
    }

    private data class TestGood(
        private val ram: Int = 16,
        private val os: OS = OS.MAC,
        private val displaySize: Double = 13.0,
        private val processor: ProcessorType = ProcessorType.M1,
        private val price: Double = 2000.0
    ) : Good {

        override fun <T> map(mapper: Good.Mapper<T>): T =
            mapper.map(ram, os, displaySize, processor, price)
    }
}