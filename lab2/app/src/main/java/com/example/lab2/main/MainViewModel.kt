package com.example.lab2.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab2.data.models.CurrencyResponse
import com.example.lab2.util.DispatcherProvider
import com.example.lab2.util.Resource
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
        class Success(val resultText: String): CurrencyEvent()
        class Failure(val errorText: String): CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(amountStr: String, fromCurrency: String, toCurrency: String) {
        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading

            when (val response = repository.getRates()) {
                is Resource.Error -> {
                    _conversion.value = CurrencyEvent.Failure(response.message ?: "Unknown error")
                }
                is Resource.Success -> {
                    val data: CurrencyResponse = response.data!!
                    val fromRate = if (fromCurrency == "RUB") 1.0 else data.valute[fromCurrency]?.value
                    val fromNominal = if (fromCurrency == "RUB") 1 else data.valute[fromCurrency]?.nominal
                    val toRate = if (toCurrency == "RUB") 1.0 else data.valute[toCurrency]?.value
                    val toNominal = if (toCurrency == "RUB") 1 else data.valute[toCurrency]?.nominal

                    if (fromRate == null || fromNominal == null || toRate == null || toNominal == null) {
                        _conversion.value = CurrencyEvent.Failure("Currency not found")
                        return@launch
                    }

                    val converted = round((fromAmount * fromRate / fromNominal * toNominal / toRate) * 100) / 100

                    _conversion.value = CurrencyEvent.Success(
                        "$fromAmount $fromCurrency = $converted $toCurrency"
                    )
                }
            }
        }
    }
}
