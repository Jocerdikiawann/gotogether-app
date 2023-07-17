package com.example.livetracking.repository.design

import com.example.livetracking.RouteProto
import com.example.livetracking.RouteProto.LocationResponse
import com.example.livetracking.RouteProto.Point
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.LocationData
import kotlinx.coroutines.flow.Flow

interface RouteRepository {
    suspend fun sendLocation(
        location:Flow<RouteProto.LocationRequest>,
    ) : Flow<DataState<LocationResponse>>

    suspend fun sendDestinationAndPolyline(
        latitudeDestination:Double,
        longitudeDestination:Double,
        encodedRoute:String
    ) : Flow<DataState<RouteProto.DestintationAndPolylineResponse>>
}