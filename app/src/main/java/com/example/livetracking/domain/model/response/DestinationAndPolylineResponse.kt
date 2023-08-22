package com.example.livetracking.domain.model.response

import com.example.livetracking.domain.model.request.Destiny

data class DestinationAndPolylineResponse(
    val id:String,
    val encodedRoute:String,
    val destination: Destiny
)

