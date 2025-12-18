package com.example.lab2.main

import CurrencyResult
import android.Manifest
import androidx.annotation.RequiresPermission
import com.example.lab2.data.CurrencyRepository
import timber.log.Timber
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : MainRepository {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun getRates(): CurrencyResult {
        Timber.i("Fetching currency rates from repository")
        val result = currencyRepository.getRates()
        when (result) {
            is CurrencyResult.Success -> Timber.i("Rates fetched successfully, offline=${result.isOffline}")
            is CurrencyResult.Error -> Timber.e("Failed to fetch rates: ${result.message}")
        }
        return result
    }
}