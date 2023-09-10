package com.example.livetracking.domain.model.request

data class DestinationAndPolylineRequest(
    val destination: Destiny,
    val encodedRoute: String,
    val initialLocation: Destiny,
    val locationName:String,
    val destinationName:String,
    val estimateTime:String,
)

data class Destiny(
    val latitude:Double,
    val longitude:Double,
)