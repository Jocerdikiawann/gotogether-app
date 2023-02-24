package com.example.livetracking.data.remote.design

import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.response.GeocodingResponse
import com.example.livetracking.domain.model.response.GoogleMapsInfoModel
import com.example.livetracking.domain.utils.TravelModes

interface GoogleDataSource {
    suspend fun getDirection(
        origin: String,
        mode: TravelModes,
        destination: String
    ): DataState<GoogleMapsInfoModel>

    suspend fun geocodingLocation(
        latlng:String
    ): DataState<GeocodingResponse>
}