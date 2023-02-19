package com.example.livetracking.data.coroutines

import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    fun main() = Dispatchers.Main
    fun default() = Dispatchers.Default
    fun io() = Dispatchers.IO
    fun unconfined() = Dispatchers.Unconfined
}

class DefaultDispatcherProvider : DispatcherProvider