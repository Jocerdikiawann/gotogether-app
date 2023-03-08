package com.example.livetracking.domain.model.response

import com.example.livetracking.domain.utils.ErrorStatus

data class RoutesResponse(
    val routes: List<Route>? = null,
    val error: ErrorResponse? = null
)

data class Route(
    val distanceMeters: Long,
    val duration: String,
    val polyline: Polyline
)

data class Polyline(
    val encodedPolyline: String
)

data class ErrorResponse(
    val code: Long,
    val message: String,
    val status: ErrorStatus,
)