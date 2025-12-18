package com.example.lab2.di

import com.example.lab2.data.CurrencyApi
import com.example.lab2.data.CurrencyRepository
import com.example.lab2.main.DefaultMainRepository
import com.example.lab2.main.MainRepository
import com.example.lab2.util.DispatcherProvider
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://www.cbr-xml-daily.ru/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCurrencyApi(): CurrencyApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideMainRepository(
        currencyRepository: CurrencyRepository
    ): MainRepository =
        DefaultMainRepository(currencyRepository)

    @Provides
    @Singleton
    fun provideDispatchers(): DispatcherProvider =
        object : DispatcherProvider {
            override val main = Dispatchers.Main
            override val io = Dispatchers.IO
            override val default = Dispatchers.Default
            override val unconfined = Dispatchers.Unconfined
        }
}