package com.example.livetracking.repository.design

import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.request.Destiny
import com.example.livetracking.domain.model.response.DestinationAndPolylineResponse
import kotlinx.coroutines.flow.Flow

interface RouteRepository {

    suspend fun sendDestinationAndPolyline(
        destination:Destiny,
        initialLocation:Destiny,
        encodedRoute: String
    ): Flow<DataState<DestinationAndPolylineResponse>>
}