package com.example.livetracking.domain.model.request

import com.example.livetracking.domain.model.LocationData

data class LocationRequest(
    val id:Int,
    val data: LocationData,
    val azimuth: Float,
)