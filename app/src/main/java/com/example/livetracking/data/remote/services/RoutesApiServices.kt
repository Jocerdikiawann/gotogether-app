package com.example.livetracking.data.remote.services

import com.example.livetracking.domain.model.request.RoutesRequest
import com.example.livetracking.domain.model.response.RoutesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RoutesApiServices {
    @POST("/directions/v2:computeRoutes")
    suspend fun getRoutesDirection(
        @Body requestBody: RoutesRequest,
        @Header("X-Goog-FieldMask") fieldMask: String
    ): Response<RoutesResponse>


}