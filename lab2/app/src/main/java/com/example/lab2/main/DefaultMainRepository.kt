package com.example.lab2.main

import CurrencyResult
import android.Manifest
import androidx.annotation.RequiresPermission
import com.example.lab2.data.CurrencyRepository
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : MainRepository {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun getRates(): CurrencyResult {
        return currencyRepository.getRates()
    }
}