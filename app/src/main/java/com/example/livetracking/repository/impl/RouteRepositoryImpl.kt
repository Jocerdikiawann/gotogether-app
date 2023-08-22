package com.example.livetracking.repository.impl

import com.example.livetracking.data.coroutines.DispatcherProvider
import com.example.livetracking.data.local.room.TokenDao
import com.example.livetracking.data.local.room.UserDao
import com.example.livetracking.data.remote.design.ShareTripDataSource
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.request.DestinationAndPolylineRequest
import com.example.livetracking.domain.model.request.Destiny
import com.example.livetracking.domain.model.response.DestinationAndPolylineResponse
import com.example.livetracking.repository.design.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RouteRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val tokenDao: TokenDao,
    private val shareTripDataSource: ShareTripDataSource,
) : RouteRepository {
    override suspend fun sendDestinationAndPolyline(
        destination:Destiny,
        initialLocation:Destiny,
        encodedRoute: String
    ): Flow<DataState<DestinationAndPolylineResponse>> =
        flow {
            emit(DataState.onLoading)
            when (val result = shareTripDataSource.sendDestinationAndPolyline(
                DestinationAndPolylineRequest(
                    destination = destination,
                    encodedRoute = encodedRoute,
                    initialLocation = initialLocation
                ),
                tokenDao.getToken()?.token.orEmpty()
            )) {
                is DataState.onData -> if (result.data.success == true && result.data.data != null) {
                    emit(DataState.onData(result.data.data))
                } else {
                    emit(DataState.onFailure(result.data.message))
                }

                is DataState.onFailure -> emit(DataState.onFailure(result.message))

                DataState.onLoading -> emit(DataState.onLoading)
            }
        }.flowOn(dispatcherProvider.main())
}