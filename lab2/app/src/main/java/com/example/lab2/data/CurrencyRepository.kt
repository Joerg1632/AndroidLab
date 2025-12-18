package com.example.lab2.data

import CurrencyResult
import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.example.lab2.data.cache.CurrencyCache
import com.example.lab2.data.models.CurrencyResponse
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val api: CurrencyApi,
    private val cache: CurrencyCache,
    private val gson: Gson,
    @ApplicationContext private val context: Context
) {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    suspend fun getRates(): CurrencyResult {
        return if (isNetworkAvailable()) {
            try {
                val response = api.getRates()

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!

                    cache.saveRates(gson.toJson(data))

                    CurrencyResult.Success(
                        data = data,
                        isOffline = false
                    )
                } else {
                    loadFromCacheOrError()
                }

            } catch (e: Exception) {
                loadFromCacheOrError()
            }
        } else {
            loadFromCacheOrError()
        }
    }

    private fun loadFromCacheOrError(): CurrencyResult {
        val cachedJson = cache.getRates()

        return if (cachedJson != null) {
            val cachedData =
                gson.fromJson(cachedJson, CurrencyResponse::class.java)

            CurrencyResult.Success(
                data = cachedData,
                isOffline = true
            )
        } else {
            CurrencyResult.Error(
                message = "Нет интернета и нет сохранённых данных"
            )
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )
    }
}
