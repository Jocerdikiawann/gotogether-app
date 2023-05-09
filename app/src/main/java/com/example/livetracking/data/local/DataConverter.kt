package com.example.livetracking.data.local

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

object DataConverter {
    @TypeConverter
    @JvmStatic
    fun fromLatLng(data: LatLng?): String {
        if (data == null) return ""
        val gson = Gson()
        return gson.toJson(data)

    }

    @TypeConverter
    @JvmStatic
    fun toLatLng(data: String?): LatLng? {
        if (data == null) return null
        if (data == "") return null
        val gson = Gson()
        return gson.fromJson(data, LatLng::class.java)
    }
}