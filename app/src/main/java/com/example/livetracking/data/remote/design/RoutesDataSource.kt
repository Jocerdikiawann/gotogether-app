package com.example.livetracking.data.remote.design

import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.request.RoutesRequest
import com.example.livetracking.domain.model.response.RoutesResponse

interface RoutesDataSource {
    suspend fun getRoutesDirection(
        body:RoutesRequest,
        fieldMask:String
    ) : DataState<RoutesResponse>
}