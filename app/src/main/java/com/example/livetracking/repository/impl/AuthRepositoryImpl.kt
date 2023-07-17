package com.example.livetracking.repository.impl

import com.example.livetracking.AuthGrpcKt
import com.example.livetracking.AuthProto.UserResponse
import com.example.livetracking.data.coroutines.DispatcherProvider
import com.example.livetracking.data.local.room.TokenDao
import com.example.livetracking.data.local.room.UserDao
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.entity.TokenEntity
import com.example.livetracking.domain.entity.UserEntity
import com.example.livetracking.repository.design.AuthRepository
import com.example.livetracking.userRequest
import io.grpc.ManagedChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepositoryImpl(
    private val channel: ManagedChannel,
    private val dispatcherProvider: DispatcherProvider,
    private val userDao: UserDao,
    private val tokenDao: TokenDao,
) : AuthRepository {
    override suspend fun signInWithGoogle(
        id: String,
        email: String,
        fullName: String
    ): Flow<DataState<UserResponse>> = flow {
        emit(DataState.onLoading)
        try {
            val stub = AuthGrpcKt.AuthCoroutineStub(channel)
            val request = userRequest {
                this.googleId = id
                this.email = email
                this.name = fullName
            }
            val response = stub.signUp(request)
            if (response.success) {
                userDao.insert(
                    UserEntity(
                        id = 1,
                        googleId = id,
                        fullName = fullName,
                        email = email,
                    )
                )
                tokenDao.insert(
                    TokenEntity(
                        id = 1,
                        token = response.token
                    )
                )
                emit(DataState.onData(response))
            } else {
                emit(DataState.onFailure(response.message))
            }
        } catch (e: Exception) {
            emit(DataState.onFailure(e.message ?: "Internal Error"))
        } finally {
            channel.shutdownNow()
        }
    }.flowOn(dispatcherProvider.io())

    override suspend fun checkIsLoggedIn(): Flow<Boolean> = flow<Boolean> {
        val user = userDao.getUser()
        if (user == null) {
            emit(false)
        } else {
            emit(true)
        }
    }.flowOn(dispatcherProvider.main())

    override suspend fun getUser(): UserEntity {
        return userDao.getUser() ?: UserEntity(googleId = "", email = "", fullName = "")
    }
}