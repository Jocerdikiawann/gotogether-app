package com.example.livetracking.data.remote.impl

import com.example.livetracking.data.remote.design.RoutesDataSource
import com.example.livetracking.data.remote.services.RoutesApiServices
import com.example.livetracking.data.utils.safeApiCall
import com.example.livetracking.domain.model.request.RoutesRequest

class RoutesDataSourceImpl(
    private val apiServices: RoutesApiServices
) : RoutesDataSource {
    override suspend fun getRoutesDirection(
        body: RoutesRequest,
        fieldMask: String
    ) = safeApiCall {
        apiServices.getRoutesDirection(body, fieldMask)
    }
}