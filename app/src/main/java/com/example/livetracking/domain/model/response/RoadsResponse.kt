package com.example.livetracking.domain.model.response

data class RoadsResponse (
    val snappedPoints: List<SnappedPoint>,
    val error: ErrorResponse? = null
)

data class SnappedPoint (
    val location: Locations,
    val originalIndex: Long? = null,
    val placeID: String
)

data class Locations (
    val latitude: Double,
    val longitude: Double
)