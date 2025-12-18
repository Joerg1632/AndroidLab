package com.example.lab2.di

import android.content.Context
import com.example.lab2.favorites.FavoritesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoritesModule {

    @Provides
    @Singleton
    fun provideFavoritesRepository(@ApplicationContext context: Context): FavoritesRepository {
        return FavoritesRepository(context)
    }
}
