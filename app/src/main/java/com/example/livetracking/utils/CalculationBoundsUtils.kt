package com.example.livetracking.utils

import com.google.android.gms.maps.model.LatLng
import kotlin.math.cos

internal fun CalculationBoundsUtils(lat: Double, lng: Double, dy: Long, dx: Long): LatLng {
    //6378137 adalah jari-jari rata-rata bumi dalam meter
    val calculationLat = lat + (180 / Math.PI) * (dy / 6378137)
    val calculationLng = lng + (180 / Math.PI) * (dx / 6378137) / cos(lat)
    return LatLng(calculationLat, calculationLng)

}