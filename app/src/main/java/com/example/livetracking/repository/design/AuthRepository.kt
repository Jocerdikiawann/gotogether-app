package com.example.livetracking.repository.design

import com.example.livetracking.AuthProto.UserResponse
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithGoogle(
        id: String,
        email: String,
        fullName: String
    ): Flow<DataState<UserResponse>>

    suspend fun checkIsLoggedIn() : Flow<Boolean>

    suspend fun getUser() : UserEntity
}