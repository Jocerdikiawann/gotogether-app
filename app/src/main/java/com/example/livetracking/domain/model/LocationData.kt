package com.example.livetracking.domain.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class LocationData(
    val lat: Double=0.0,
    val lng: Double=0.0,
)

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)