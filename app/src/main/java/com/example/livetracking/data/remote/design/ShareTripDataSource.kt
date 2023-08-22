package com.example.livetracking.data.remote.design

import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.request.AuthRequest
import com.example.livetracking.domain.model.request.DestinationAndPolylineRequest
import com.example.livetracking.domain.model.response.AuthResponse
import com.example.livetracking.domain.model.response.BaseResponseShareTrip
import com.example.livetracking.domain.model.response.DestinationAndPolylineResponse

interface ShareTripDataSource {
    suspend fun authentication(
        request: AuthRequest,
    ): DataState<BaseResponseShareTrip<AuthResponse>>

    suspend fun sendDestinationAndPolyline(
        request: DestinationAndPolylineRequest,
        token: String
    ): DataState<BaseResponseShareTrip<DestinationAndPolylineResponse>>
}