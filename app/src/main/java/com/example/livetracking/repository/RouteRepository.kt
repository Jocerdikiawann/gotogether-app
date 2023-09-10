package com.example.livetracking.repository

import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.request.Destiny
import com.example.livetracking.domain.model.response.DestinationAndPolylineResponse
import kotlinx.coroutines.flow.Flow

interface RouteRepository {

    suspend fun sendDestinationAndPolyline(
        destination:Destiny,
        initialLocation:Destiny,
        encodedRoute: String,
        destinationName:String,
        locationName:String,
        estimateTime:String
    ): Flow<DataState<DestinationAndPolylineResponse>>

    suspend fun savePlacesId(
        id:String,
    ): Flow<Unit>

    suspend fun getPlacesId() : Flow<DataState<String>>
}