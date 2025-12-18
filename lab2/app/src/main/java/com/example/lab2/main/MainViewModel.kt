package com.example.lab2.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab2.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent {
        data class Success(
            val resultText: String,
            val isOffline: Boolean
        ) : CurrencyEvent()

        data class Failure(val errorText: String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(amountStr: String, fromCurrency: String, toCurrency: String) {
        val fromAmount = amountStr.toDoubleOrNull()
        if (fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            Timber.e("Invalid input for conversion: '$amountStr'")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            Timber.d("Starting conversion: $fromAmount $fromCurrency -> $toCurrency")

            try {
                val result = repository.getRates()

                when (result) {
                    is CurrencyResult.Error -> {
                        _conversion.value = CurrencyEvent.Failure(result.message)
                        Timber.e("Failed to fetch rates: ${result.message}")
                    }

                    is CurrencyResult.Success -> {
                        val data = result.data
                        if (data == null) {
                            _conversion.value = CurrencyEvent.Failure("No data received")
                            Timber.e("CurrencyResponse is null")
                            return@launch
                        }

                        val fromRate = if (fromCurrency == "RUB") 1.0 else data.valute[fromCurrency]?.value
                        val fromNominal = if (fromCurrency == "RUB") 1 else data.valute[fromCurrency]?.nominal
                        val toRate = if (toCurrency == "RUB") 1.0 else data.valute[toCurrency]?.value
                        val toNominal = if (toCurrency == "RUB") 1 else data.valute[toCurrency]?.nominal

                        if (fromRate == null || fromNominal == null || toRate == null || toNominal == null) {
                            _conversion.value = CurrencyEvent.Failure("Currency not found")
                            Timber.e("Missing currency data: fromRate=$fromRate, fromNominal=$fromNominal, toRate=$toRate, toNominal=$toNominal")
                            return@launch
                        }

                        val converted = round(fromAmount * fromRate / fromNominal * toNominal / toRate * 100) / 100
                        val resultText = "$fromAmount $fromCurrency = $converted $toCurrency"

                        _conversion.value = CurrencyEvent.Success(resultText, result.isOffline)
                        Timber.i("Conversion successful: $resultText (offline=${result.isOffline})")
                    }
                }
            } catch (e: Exception) {
                _conversion.value = CurrencyEvent.Failure("Error occurred: ${e.message}")
                Timber.e(e, "Exception during conversion")
            }
        }
    }
}
