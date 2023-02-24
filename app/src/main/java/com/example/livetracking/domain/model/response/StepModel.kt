package com.example.livetracking.domain.model.response

data class StepModel(
    val distance: DistanceModel,
    val duration: DistanceModel,
    val end_location: LatitudeLongitudeModel,
    val html_instructions: String,
    val polyline: PolylineModel,
    val start_location: LatitudeLongitudeModel,
    val travel_mode: TravelMode,
    val maneuver: String? = null
)

enum class TravelMode {
    BICYCLING,
    DRIVING,
    TRANSIT,
    WALKING
}