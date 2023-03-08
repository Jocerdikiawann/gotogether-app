package com.example.livetracking.domain.model.request

import com.example.livetracking.domain.utils.RouteTravelModes
import com.example.livetracking.domain.utils.RoutingPreference

data class RoutesRequest (
    val origin: Destination,
    val destination: Destination,
    val travelMode: RouteTravelModes,
    val routingPreference: RoutingPreference,
    val departureTime: String,
    val computeAlternativeRoutes: Boolean,
    val routeModifiers: RouteModifiers,
    val languageCode: String,
    val units: String
)

data class Destination (
    val location: Loc
)

data class Loc (
    val latLng: LocLatLng
)

data class LocLatLng (
    val latitude: Double,
    val longitude: Double
)

data class RouteModifiers (
    val avoidTolls: Boolean,
    val avoidHighways: Boolean,
    val avoidFerries: Boolean
)
