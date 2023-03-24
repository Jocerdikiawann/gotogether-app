package com.example.livetracking.data.remote.services

import com.example.livetracking.domain.model.response.RoadsResponse
import retrofit2.Response
import retrofit2.http.GET

interface RoadsApiServices {
    @GET("/snapToRoads")
    suspend fun snapToRoads(

    ) : Response<RoadsResponse>
}