package com.example.lab2.favorites

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)

    private val FAVORITES_KEY = "favorites"

    fun addFavorite(code: String) {
        val current = prefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        prefs.edit().putStringSet(FAVORITES_KEY, current + code).apply()
    }

    fun removeFavorite(code: String) {
        val current = prefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        prefs.edit().putStringSet(FAVORITES_KEY, current - code).apply()
    }

    fun getFavorites(): Set<String> {
        return prefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
    }
}