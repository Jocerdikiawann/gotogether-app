package com.example.livetracking.domain.model.response

import com.example.livetracking.domain.utils.ErrorStatus

data class GoogleMapsInfoModel(
    val error_message: String? = null,
    val geocoded_waypoints: List<GeocodedWaypointModel>? = null,
    val routes: List<RouteModel>,
    val status: ErrorStatus
)