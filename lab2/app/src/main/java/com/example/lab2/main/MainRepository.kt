package com.example.lab2.main

import com.example.lab2.data.models.CurrencyResponse
import com.example.lab2.util.Resource

interface MainRepository {
    suspend fun getRates(): Resource<CurrencyResponse>
}
