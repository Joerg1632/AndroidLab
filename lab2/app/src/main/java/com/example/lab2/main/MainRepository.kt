package com.example.lab2.main

import CurrencyResult
import com.example.lab2.data.models.CurrencyResponse
import com.example.lab2.util.Resource

interface MainRepository {
    suspend fun getRates(): CurrencyResult
}