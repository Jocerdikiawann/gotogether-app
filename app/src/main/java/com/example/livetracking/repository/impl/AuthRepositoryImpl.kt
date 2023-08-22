package com.example.livetracking.repository.impl

import com.example.livetracking.data.coroutines.DispatcherProvider
import com.example.livetracking.data.local.room.TokenDao
import com.example.livetracking.data.local.room.UserDao
import com.example.livetracking.data.remote.design.ShareTripDataSource
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.entity.TokenEntity
import com.example.livetracking.domain.entity.UserEntity
import com.example.livetracking.domain.model.request.AuthRequest
import com.example.livetracking.repository.design.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.ConnectException

class AuthRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val userDao: UserDao,
    private val tokenDao: TokenDao,
    private val shareTripDataSource: ShareTripDataSource,
) : AuthRepository {
    override suspend fun signInWithGoogle(
        id: String,
        email: String,
        fullName: String
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.onLoading)
        try {
            when (val result = shareTripDataSource.authentication(
                AuthRequest(
                    id, email, fullName
                )
            )) {
                DataState.onLoading -> emit(DataState.onLoading)
                is DataState.onData -> {
                    if (result.data.success == true) {
                        val data = result.data.data
                        tokenDao.insert(
                            TokenEntity(
                                token = result.data.token.orEmpty()
                            )
                        )
                        userDao.insert(
                            UserEntity(
                                googleId = data?.googleId.orEmpty(),
                                fullName = data?.name.orEmpty(),
                                email = data?.email.orEmpty(),
                            )
                        )
                        emit(DataState.onData(true))
                    } else {
                        emit(DataState.onFailure(result.data.message))
                    }
                }

                is DataState.onFailure -> emit(DataState.onFailure(result.message))
            }
        } catch (e: ConnectException) {
            emit(DataState.onFailure("Internal Server Error"))
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