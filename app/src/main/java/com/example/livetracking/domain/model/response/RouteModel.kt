package com.example.livetracking.domain.model.response

data class RouteModel (
    val bounds: BoundsModel,
    val copyrights: String,
    val legs: List<LegModel>,
    val overview_polyline: PolylineModel,
    val summary: String,
    val warnings: List<Any?>,
    val waypoint_order: List<Any?>
)