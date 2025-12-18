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
import timber.log.Timber
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
            Timber.d("Network available. Fetching rates from API")
            try {
                val response = api.getRates()
                Timber.i("Rates fetched successfully from API")

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!

                    cache.saveRates(gson.toJson(data))
                    Timber.d("Rates saved to cache")

                    CurrencyResult.Success(
                        data = data,
                        isOffline = false
                    )
                } else {
                    loadFromCacheOrError()
                }

            } catch (e: Exception) {
                Timber.e(e, "Failed to fetch rates from API. Trying cache")
                loadFromCacheOrError()
            }
        } else {
            Timber.w("No network available. Loading rates from cache")
            loadFromCacheOrError()
        }
    }

    private fun loadFromCacheOrError(): CurrencyResult {
        val cachedJson = cache.getRates()

        return if (cachedJson != null) {
            Timber.i("Loaded rates from cache")
            val cachedData =
                gson.fromJson(cachedJson, CurrencyResponse::class.java)

            CurrencyResult.Success(
                data = cachedData,
                isOffline = true
            )
        } else {
            Timber.e("No cached data available")
            CurrencyResult.Error(message = "No internet and no cache data")
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isNetworkAvailable(): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = cm.activeNetwork
            if (network == null) {
                Timber.w("No active network found")
                return false
            }

            val capabilities = cm.getNetworkCapabilities(network)
            if (capabilities == null) {
                Timber.w("No network capabilities found")
                return false
            }

            val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            Timber.d("Network available: $hasInternet")
            hasInternet
        } catch (e: Exception) {
            Timber.e(e, "Error checking network availability")
            false
        }
    }
}
