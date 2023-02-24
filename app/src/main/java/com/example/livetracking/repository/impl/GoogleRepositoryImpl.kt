package com.example.livetracking.repository.impl

import com.example.livetracking.data.coroutines.DispatcherProvider
import com.example.livetracking.data.remote.design.GoogleDataSource
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.response.GeocodingResponse
import com.example.livetracking.domain.model.response.GoogleMapsInfoModel
import com.example.livetracking.domain.utils.TravelModes
import com.example.livetracking.repository.design.GoogleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GoogleRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val googleDataSource: GoogleDataSource
) : GoogleRepository {
    override suspend fun getDirection(
        origin: String,
        mode: TravelModes,
        destination: String
    ): Flow<DataState<GoogleMapsInfoModel>> = flow {
        emit(DataState.onLoading)
        try {
            when (val result = googleDataSource.getDirection(origin, mode, destination)) {
                is DataState.onData -> {
                    emit(DataState.onData(result.data))
                }
                is DataState.onFailure -> emit(DataState.onFailure(result.error_message))
                DataState.onLoading -> emit(DataState.onLoading)
            }
        } catch (e: HttpException) {
            emit(DataState.onFailure("Something when wrong"))
        } catch (e: IOException) {
            emit(DataState.onFailure("No internet connection"))
        }
    }

    override suspend fun geocodingLocation(latlng: String): Flow<DataState<GeocodingResponse>> =
        flow {
            emit(DataState.onLoading)
            try {
                when (val result = googleDataSource.geocodingLocation(latlng)) {
                    is DataState.onData -> {
                        emit(DataState.onData(result.data))
                    }
                    is DataState.onFailure -> {
                        emit(DataState.onFailure(result.error_message))
                    }
                    DataState.onLoading -> emit(DataState.onLoading)
                }
            } catch (e: HttpException) {
                emit(DataState.onFailure("Something when wrong"))
            } catch (e: IOException) {
                emit(DataState.onFailure("No internet connection"))
            }
        }

}