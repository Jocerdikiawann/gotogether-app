package com.example.livetracking.repository.impl

import com.example.livetracking.data.coroutines.DispatcherProvider
import com.example.livetracking.data.local.Persistence
import com.example.livetracking.data.remote.design.ShareTripDataSource
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.request.DestinationAndPolylineRequest
import com.example.livetracking.domain.model.request.Destiny
import com.example.livetracking.domain.model.response.DestinationAndPolylineResponse
import com.example.livetracking.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RouteRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val persistence: Persistence,
    private val shareTripDataSource: ShareTripDataSource,
) : RouteRepository {
    override suspend fun sendDestinationAndPolyline(
        destination: Destiny,
        initialLocation: Destiny,
        encodedRoute: String,
        destinationName:String,
        locationName:String,
        estimateTime:String
    ): Flow<DataState<DestinationAndPolylineResponse>> =
        flow {
            emit(DataState.onLoading)
            when (val result = shareTripDataSource.sendDestinationAndPolyline(
                DestinationAndPolylineRequest(
                    destination = destination,
                    encodedRoute = encodedRoute,
                    initialLocation = initialLocation,
                    destinationName = destinationName,
                    estimateTime = estimateTime,
                    locationName = locationName,
                ),
                persistence.getToken().orEmpty()
            )) {
                is DataState.onData -> if (result.data.success == true && result.data.data != null) {
                    emit(DataState.onData(result.data.data))
                } else {
                    emit(DataState.onFailure(result.data.message))
                }

                is DataState.onFailure -> emit(DataState.onFailure(result.message))

                DataState.onLoading -> emit(DataState.onLoading)
            }
        }.flowOn(dispatcherProvider.io())

    override suspend fun savePlacesId(id: String): Flow<Unit> = flow<Unit> {
        emit(persistence.setPlaceId(id))
    }.flowOn(dispatcherProvider.io())

    override suspend fun getPlacesId(): Flow<DataState<String>> = flow {
        emit(DataState.onLoading)
        persistence.getPlaceId()?.let {
            emit(DataState.onData(it))
        } ?: kotlin.run {
            emit(DataState.onFailure("Empty PlaceId"))
        }
    }.flowOn(dispatcherProvider.io())
}