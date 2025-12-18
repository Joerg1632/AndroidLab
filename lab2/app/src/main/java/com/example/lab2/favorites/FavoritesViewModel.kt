package com.example.lab2.favorites

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repo: FavoritesRepository
) : ViewModel() {

    private val _favorites = MutableStateFlow<Set<String>>(repo.getFavorites())
    val favorites: StateFlow<Set<String>> = _favorites

    fun addFavorite(code: String) {
        repo.addFavorite(code)
        _favorites.value = repo.getFavorites()
    }

    fun removeFavorite(code: String) {
        repo.removeFavorite(code)
        _favorites.value = repo.getFavorites()
    }
}
