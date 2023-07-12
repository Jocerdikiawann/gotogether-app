package com.example.livetracking.ui.page.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.repository.design.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ViewModelAuth @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private var _authStateUI = MutableStateFlow(AuthStateUI())
    val authStateUI get() = _authStateUI

    fun authentication(credential: Task<GoogleSignInAccount>?) =
        viewModelScope.launch {
            try {
                if (credential == null) {
                    _authStateUI.emit(AuthStateUI(error = true, errMsg = "Failed authentication"))
                } else {
                    val account = credential.await()
                    authRepository.signInWithGoogle(
                        id = account.id ?: "",
                        email = account.email ?: "",
                        fullName = account.displayName ?: "",
                    ).onEach {
                        _authStateUI.emit(
                            when (it) {
                                is DataState.onData -> {
                                    AuthStateUI(
                                        data = it.data
                                    )
                                }

                                is DataState.onFailure -> {
                                    AuthStateUI(
                                        error = true,
                                        errMsg = it.error_message
                                    )
                                }

                                DataState.onLoading -> {
                                    AuthStateUI(loading = true)
                                }
                            }
                        )
                    }.collect()
                }
            } catch (e: Exception) {
                _authStateUI.emit(
                    AuthStateUI(
                        error = true,
                        errMsg = "Failed authentication : ${e.message}"
                    )
                )
            }
        }
}