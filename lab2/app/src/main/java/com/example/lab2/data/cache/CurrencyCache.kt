package com.example.lab2.data.cache

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyCache @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("currency_cache", Context.MODE_PRIVATE)

    private companion object {
        const val KEY_RATES_JSON = "rates_json"
        const val KEY_TIMESTAMP = "rates_timestamp"
    }

    fun saveRates(json: String) {
        prefs.edit()
            .putString(KEY_RATES_JSON, json)
            .putLong(KEY_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }

    fun getRates(): String? =
        prefs.getString(KEY_RATES_JSON, null)

    fun getTimestamp(): Long =
        prefs.getLong(KEY_TIMESTAMP, 0L)

    fun hasCache(): Boolean =
        prefs.contains(KEY_RATES_JSON)
}
