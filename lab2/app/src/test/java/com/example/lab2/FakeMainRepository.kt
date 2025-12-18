package com.example.lab2

import CurrencyResult
import com.example.lab2.data.models.CurrencyItem
import com.example.lab2.data.models.CurrencyResponse
import com.example.lab2.main.MainRepository
import kotlinx.coroutines.delay

class FakeMainRepository : MainRepository {

    override suspend fun getRates(): CurrencyResult {
        delay(50)
        val fakeData = CurrencyResponse(
            valute = mapOf(
                "USD" to CurrencyItem("USD", 1, 1.0),
                "RUB" to CurrencyItem("RUB", 1, 80.0),
                "EUR" to CurrencyItem("EUR", 1, 0.9)
            )
        )
        return CurrencyResult.Success(fakeData, isOffline = false)
    }
}
