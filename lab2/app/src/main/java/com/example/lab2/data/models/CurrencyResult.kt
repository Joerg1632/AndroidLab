import com.example.lab2.data.models.CurrencyResponse

sealed class CurrencyResult {
    data class Success(
        val data: CurrencyResponse,
        val isOffline: Boolean
    ) : CurrencyResult()

    data class Error(
        val message: String
    ) : CurrencyResult()
}
