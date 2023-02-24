package com.example.livetracking.domain.model.response

data class BoundsModel(
    val northeast: LatitudeLongitudeModel,
    val southwest: LatitudeLongitudeModel
)