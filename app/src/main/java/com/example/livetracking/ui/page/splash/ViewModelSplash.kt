package com.example.livetracking.ui.page.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.repository.design.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelSplash @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private var _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn get() = _isLoggedIn

    init {
        checkIsLoggedIn()
    }

    private fun checkIsLoggedIn() = viewModelScope.launch {
        authRepository.checkIsLoggedIn().onEach {
            _isLoggedIn.emit(it)
        }.collect()
    }
}