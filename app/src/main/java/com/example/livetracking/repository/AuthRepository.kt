package com.example.livetracking.repository

import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithGoogle(
        id: String,
        email: String,
        fullName: String
    ): Flow<DataState<Boolean>>

    suspend fun checkIsLoggedIn() : Flow<Boolean>

    suspend fun getUser() : Flow<UserEntity>
}