package com.example.livetracking.data.remote.services

import com.example.livetracking.domain.model.request.AuthRequest
import com.example.livetracking.domain.model.request.DestinationAndPolylineRequest
import com.example.livetracking.domain.model.response.AuthResponse
import com.example.livetracking.domain.model.response.BaseResponseShareTrip
import com.example.livetracking.domain.model.response.DestinationAndPolylineResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ClientShareTripApiServices {
    @POST("v1/auth/signUp")
    suspend fun authentication(
        @Body request: AuthRequest,
    ): Response<BaseResponseShareTrip<AuthResponse>>


    @POST("v1/route/sendDestinationAndPolyline")
    suspend fun sendDestinationAndPolyline(
        @Body request: DestinationAndPolylineRequest,
        @Header("authorization") token:String
    ): Response<BaseResponseShareTrip<DestinationAndPolylineResponse>>
}