package com.example.lab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab2.databinding.ActivityMainBinding
import com.example.lab2.favorites.FavoritesViewModel
import com.example.lab2.main.CurrencyAdapter
import com.example.lab2.main.MainViewModel
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val favoritesViewModel: FavoritesViewModel by viewModels()

    private var fromCurrency: String = "USD"
    private var toCurrency: String = "RUB"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvFromCurrency.text = fromCurrency
        binding.tvToCurrency.text = toCurrency

        binding.tvFromCurrency.setOnClickListener {
            showCurrencyPopup(it) { code ->
                Timber.i("From currency selected: $code")
                fromCurrency = code
                binding.tvFromCurrency.text = code
            }
        }

        binding.tvToCurrency.setOnClickListener {
            showCurrencyPopup(it) { code ->
                Timber.i("To currency selected: $code")
                toCurrency = code
                binding.tvToCurrency.text = code
            }
        }

        binding.btnConvert.setOnClickListener {
            val amountStr = binding.etFrom.text.toString()
            if (amountStr.isBlank()) {
                binding.tvResult.setTextColor(getColor(R.color.errorColor))
                binding.tvResult.text = "Enter amount"
                Timber.w("Convert clicked with empty amount")
                return@setOnClickListener
            }
            Timber.i("Converting $amountStr $fromCurrency to $toCurrency")
            viewModel.convert(amountStr, fromCurrency, toCurrency)
        }

        binding.btnSwap.setOnClickListener {
            val temp = fromCurrency
            fromCurrency = toCurrency
            toCurrency = temp
            binding.tvFromCurrency.text = fromCurrency
            binding.tvToCurrency.text = toCurrency

            val amountStr = binding.etFrom.text.toString()
            if (amountStr.isNotBlank()) {
                Timber.i("Swap currencies and convert $amountStr $fromCurrency to $toCurrency")
                viewModel.convert(amountStr, fromCurrency, toCurrency)
            } else {
                binding.tvResult.setTextColor(getColor(R.color.errorColor))
                binding.tvResult.text = "Enter amount"
                Timber.w("Swap clicked but amount is empty")
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collectLatest { event ->
                binding.progressBar.isVisible = event is MainViewModel.CurrencyEvent.Loading

                when (event) {
                    is MainViewModel.CurrencyEvent.Success -> {
                        binding.tvResult.text = event.resultText
                        binding.tvOffline.isVisible = event.isOffline

                        val color = MaterialColors.getColor(
                            binding.tvResult,
                            com.google.android.material.R.attr.colorOnSurface
                        )
                        binding.tvResult.setTextColor(color)
                    }

                    is MainViewModel.CurrencyEvent.Failure -> {
                        Timber.e("Conversion failed: ${event.errorText}")
                        binding.tvResult.setTextColor(getColor(R.color.errorColor))
                        binding.tvResult.text = event.errorText
                        binding.tvOffline.isVisible = false
                    }

                    else -> {
                        binding.tvOffline.isVisible = false
                    }
                }
            }
        }
    }

    private fun showCurrencyPopup(anchor: View, onSelect: (String) -> Unit) {
        val popupView = LayoutInflater.from(this).inflate(R.layout.popup_currency_list, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val rv = popupView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvCurrencyPopup)
        rv.layoutManager = LinearLayoutManager(this)

        val currencies = resources.getStringArray(R.array.currency_codes).toList()
        val favs = favoritesViewModel.favorites.value.toMutableSet()

        val sortedCurrencies = currencies.sortedByDescending { favs.contains(it) }

        lateinit var adapter: CurrencyAdapter
        adapter = CurrencyAdapter(
            items = sortedCurrencies,
            favorites = favs,
            onFavoriteClick = { code ->
                Timber.i("Favorite clicked: $code")
                if (favoritesViewModel.favorites.value.contains(code)) {
                    favoritesViewModel.removeFavorite(code)
                    Timber.d("Removed $code from favorites")
                } else {
                    favoritesViewModel.addFavorite(code)
                    Timber.d("Added $code to favorites")
                }
                adapter.updateData(currencies, favoritesViewModel.favorites.value)
            },
            onItemClick = { code ->
                Timber.i("Currency selected from popup: $code")
                onSelect(code)
                popupWindow.dismiss()
            }
        )

        rv.adapter = adapter
        popupWindow.showAsDropDown(anchor)
    }
}
