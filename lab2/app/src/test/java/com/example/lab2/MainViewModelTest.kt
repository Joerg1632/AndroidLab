package com.example.lab2

import com.example.lab2.main.MainViewModel
import com.example.lab2.main.TestDispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private val fakeRepo = FakeMainRepository()
    private val dispatcher = TestDispatcherProvider()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel(fakeRepo, dispatcher)
    }

    @Test
    fun convert_validAmount_returnsSuccess() = runBlocking {
        viewModel.convert("100", "USD", "RUB")
        delay(10)
        val events = viewModel.conversion.take(2).toList()
        assertTrue(events.isNotEmpty())

        val lastEvent = events.last()
        assertTrue("Expected Success but got $lastEvent", lastEvent is MainViewModel.CurrencyEvent.Success)

        val result = (lastEvent as MainViewModel.CurrencyEvent.Success).resultText
        assertTrue("Result should contain 'USD =' but got: $result",
            result.contains("USD ="))
        assertTrue("Result should start with '100' but got: $result",
            result.startsWith("100"))
    }

    @Test
    fun convert_invalidAmount_returnsFailure() = runBlocking {
        viewModel.convert("abc", "USD", "RUB")

        delay(10)

        val event = viewModel.conversion.value
        assertTrue("Expected Failure but got $event", event is MainViewModel.CurrencyEvent.Failure)
        assertEquals("Not a valid amount", (event as MainViewModel.CurrencyEvent.Failure).errorText)
    }

    @Test
    fun convert_unknownCurrency_returnsFailure() = runBlocking {
        viewModel.convert("100", "USD", "XXX")

        delay(10)

        val events = viewModel.conversion.take(2).toList()
        assertTrue(events.isNotEmpty())

        val lastEvent = events.last()
        assertTrue("Expected Failure but got $lastEvent", lastEvent is MainViewModel.CurrencyEvent.Failure)
        assertEquals("Currency not found", (lastEvent as MainViewModel.CurrencyEvent.Failure).errorText)
    }

    @Test
    fun convert_zeroAmount_returnsSuccess() = runBlocking {
        viewModel.convert("0", "USD", "RUB")

        delay(10)

        val events = viewModel.conversion.take(2).toList()
        assertTrue(events.isNotEmpty())

        val lastEvent = events.last()
        assertTrue("Expected Success but got $lastEvent", lastEvent is MainViewModel.CurrencyEvent.Success)

        val result = (lastEvent as MainViewModel.CurrencyEvent.Success).resultText
        assertTrue("Result should contain '0 USD =' but got: $result", result.contains("0 USD ="))
    }
}