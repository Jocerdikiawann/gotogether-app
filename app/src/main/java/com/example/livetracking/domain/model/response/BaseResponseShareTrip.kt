package com.example.livetracking.domain.model.response

data class BaseResponseShareTrip<T>(
    val statusCode: Long,
    val success: Boolean? = null,
    val message: String,
    val data: T ? = null,
    val token: String? = null
)