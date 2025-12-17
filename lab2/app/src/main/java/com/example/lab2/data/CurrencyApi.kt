package com.example.lab2.data

import com.example.lab2.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {

    @GET("daily_json.js")
    suspend fun getRates(): Response<CurrencyResponse>
}
