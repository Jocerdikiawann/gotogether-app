package com.example.livetracking.domain.model

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

data class LocationData(
    val lat: Double=0.0,
    val lng: Double=0.0,
)

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)