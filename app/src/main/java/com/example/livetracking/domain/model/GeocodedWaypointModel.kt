package com.example.livetracking.domain.model

data class GeocodedWaypointModel(
    val geocoder_status: String,
    val partial_match: Boolean,
    val place_id: String,
    val types: List<String>
)