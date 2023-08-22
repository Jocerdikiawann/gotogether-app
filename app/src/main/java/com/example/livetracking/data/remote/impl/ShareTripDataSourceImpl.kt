package com.example.livetracking.data.remote.impl

import com.example.livetracking.data.remote.design.ShareTripDataSource
import com.example.livetracking.data.remote.services.ClientShareTripApiServices
import com.example.livetracking.data.utils.safeApiCall
import com.example.livetracking.domain.model.request.AuthRequest
import com.example.livetracking.domain.model.request.DestinationAndPolylineRequest

class ShareTripDataSourceImpl(
    private val apiServices: ClientShareTripApiServices
) : ShareTripDataSource {
    override suspend fun authentication(request: AuthRequest) = safeApiCall {
        apiServices.authentication(request)
    }

    override suspend fun sendDestinationAndPolyline(
        request: DestinationAndPolylineRequest,
        token: String
    ) = safeApiCall {
        apiServices.sendDestinationAndPolyline(request, token)
    }

}