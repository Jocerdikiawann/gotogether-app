package com.example.livetracking.data.remote.impl

import com.example.livetracking.data.remote.design.GoogleDataSource
import com.example.livetracking.data.remote.services.GoogleApiServices
import com.example.livetracking.data.utils.safeApiCall
import com.example.livetracking.domain.utils.TravelModes

class GoogleDataSourceImpl(
    private val apiServices: GoogleApiServices
) : GoogleDataSource {
    override suspend fun getDirection(
        origin: String,
        mode: TravelModes,
        destination: String
    ) = safeApiCall {
        apiServices.getDirection(
            origin,
            mode,
            destination
        )
    }

    override suspend fun geocodingLocation(latlng: String) = safeApiCall {
        apiServices.geocodingLocation(latlng)
    }
}