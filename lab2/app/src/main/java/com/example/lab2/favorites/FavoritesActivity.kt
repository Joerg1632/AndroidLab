package com.example.lab2.favorites

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab2.databinding.ActivityFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = FavoritesAdapter(emptyList()) { code ->
            viewModel.removeFavorite(code)
            Timber.i("User removed favorite: $code")
            Toast.makeText(this, "$code removed from favorites", Toast.LENGTH_SHORT).show()
        }

        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.favorites.collectLatest { favorites ->
                Timber.d("FavoritesActivity: Updating adapter with ${favorites.size} items")
                adapter.updateData(favorites.toList())
            }
        }
    }
}
