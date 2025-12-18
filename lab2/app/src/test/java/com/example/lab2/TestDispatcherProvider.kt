package com.example.lab2.main

import com.example.lab2.util.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher get() = Dispatchers.Unconfined
    override val io: CoroutineDispatcher get() = Dispatchers.Unconfined
    override val default: CoroutineDispatcher get() = Dispatchers.Unconfined
    override val unconfined: CoroutineDispatcher get() = Dispatchers.Unconfined
}
