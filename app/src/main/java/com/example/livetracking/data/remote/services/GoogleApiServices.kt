package com.example.livetracking.data.remote.services

import com.example.livetracking.domain.model.response.GeocodingResponse
import com.example.livetracking.domain.model.response.GoogleMapsInfoModel
import com.example.livetracking.domain.utils.TravelModes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface GoogleApiServices {
    @GET("/direction/json")
    suspend fun getDirection(
        @Query("origin") origin: String,
        @Query("mode") mode: TravelModes,
        @Query("destination") destination: String
    ): Response<GoogleMapsInfoModel>

    @GET("geocode/json")
    suspend fun geocodingLocation(
        @Query("latlng") latlng:String
    ):Response<GeocodingResponse>
}