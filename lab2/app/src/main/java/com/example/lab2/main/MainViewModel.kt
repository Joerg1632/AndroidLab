package com.example.lab2.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab2.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    private val _conversion =
        MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(amountStr: String, fromCurrency: String, toCurrency: String) {
        val fromAmount = amountStr.toDoubleOrNull()
        if (fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading

            when (val result = repository.getRates()) {
                is CurrencyResult.Error -> {
                    _conversion.value =
                        CurrencyEvent.Failure(result.message)
                }

                is CurrencyResult.Success -> {
                    val data = result.data

                    val fromRate =
                        if (fromCurrency == "RUB") 1.0
                        else data.valute[fromCurrency]?.value

                    val fromNominal =
                        if (fromCurrency == "RUB") 1
                        else data.valute[fromCurrency]?.nominal

                    val toRate =
                        if (toCurrency == "RUB") 1.0
                        else data.valute[toCurrency]?.value

                    val toNominal =
                        if (toCurrency == "RUB") 1
                        else data.valute[toCurrency]?.nominal

                    if (
                        fromRate == null || fromNominal == null ||
                        toRate == null || toNominal == null
                    ) {
                        _conversion.value =
                            CurrencyEvent.Failure("Currency not found")
                        return@launch
                    }

                    val converted = round(
                        fromAmount *
                                fromRate / fromNominal *
                                toNominal / toRate * 100
                    ) / 100

                    _conversion.value = CurrencyEvent.Success(
                        resultText =
                            "$fromAmount $fromCurrency = $converted $toCurrency",
                        isOffline = result.isOffline
                    )
                }
            }
        }
    }
}
