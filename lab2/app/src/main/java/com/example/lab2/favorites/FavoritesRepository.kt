package com.example.lab2.favorites

import android.content.Context
import android.content.SharedPreferences
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class FavoritesRepository @Inject constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val FAVORITES_KEY = "favorites"

    fun addFavorite(code: String) {
        val current = prefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        prefs.edit { putStringSet(FAVORITES_KEY, current + code) }
        Timber.d("Added favorite: $code. Current favorites: ${current + code}")
    }

    fun removeFavorite(code: String) {
        val current = prefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        prefs.edit { putStringSet(FAVORITES_KEY, current - code) }
        Timber.d("Removed favorite: $code. Current favorites: ${current - code}")
    }

    fun getFavorites(): Set<String> {
        val favorites = prefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        Timber.d("Retrieved favorites: $favorites")
        return favorites
    }
}