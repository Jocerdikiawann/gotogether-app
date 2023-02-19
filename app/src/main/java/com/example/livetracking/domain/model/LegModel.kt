package com.example.livetracking.domain.model

data class LegModel(
    val distance: DistanceModel,
    val duration: DistanceModel,
    val end_address: String,
    val end_location: LatitudeLongitudeModel,
    val start_address: String,
    val start_location: LatitudeLongitudeModel,
    val steps: List<StepModel>,
    val traffic_speedEntry: List<Any?>,
    val via_waypoint: List<Any?>
)