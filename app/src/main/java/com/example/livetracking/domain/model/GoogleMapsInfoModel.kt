package com.example.livetracking.domain.model

data class GoogleMapsInfoModel(
    val geocoded_waypoints: List<GeocodedWaypointModel>,
    val routes: List<RouteModel>,
    val status: String
)