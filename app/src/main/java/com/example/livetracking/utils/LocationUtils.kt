package com.example.livetracking.utils

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil

class LocationUtils(val context: Context, val DURATION_FOR_CHANGE_LOCATION: Long = 15000) :
    LiveData<Location>() {

    private var lastUpdateTime: Long = 0
    private var lastLocation: Location? = null
    private val locationHistory = mutableListOf<Location>()


    private val locationRequest: LocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, DURATION_FOR_CHANGE_LOCATION)
            .setIntervalMillis(DURATION_FOR_CHANGE_LOCATION)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun onActive() {
        super.onActive()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location.also {
                setLocationData(it)
            }
        }
        startLocationUpdate()
    }

    internal fun stopUpdateLocation() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    internal fun startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    internal fun turnOnGPS(resultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        val locationSetting = LocationSettingsRequest.Builder()
            .setAlwaysShow(true)
            .addLocationRequest(locationRequest)
            .build()

        val client = LocationServices.getSettingsClient(context.applicationContext)
            .checkLocationSettings(locationSetting)
        client.addOnSuccessListener {
            Log.e("SUCCESS1", "onGps is successes")
        }
        client.addOnFailureListener { exception ->
            when ((exception as ApiException).statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        val resolvable = exception as ResolvableApiException
                        resultLauncher.launch(
                            IntentSenderRequest.Builder(resolvable.resolution).build()
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e("ERROR", e.message.toString())
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                }
                LocationSettingsStatusCodes.SUCCESS -> {

                }
            }
        }
    }

    private fun filterLocation(
        location: Location,
        accuracyThreshold: Float = 63f,
        maxElapsedMs: Long = 500L
    ): Location? {
        val elapsedTime = SystemClock.elapsedRealtime() - lastUpdateTime
        if (lastLocation == null ||
            location.accuracy <=
            accuracyThreshold ||
            (elapsedTime >=
                    maxElapsedMs &&
                    location.accuracy <=
                    lastLocation!!.accuracy)
        ) {
            lastLocation = location
            lastUpdateTime = SystemClock.elapsedRealtime()
            return location
        }
        return null
    }

    private fun addLocationToHistory(location: Location) {
        locationHistory.add(location)

        // membatasi jumlah lokasi pada history agar tidak terlalu banyak
        if (locationHistory.size > 10) {
            locationHistory.removeAt(0)
        }
    }

    private fun getSmoothedLocation(): Location? {
        if (locationHistory.size == 0) {
            return null
        }

        val smoothedLocation = Location(locationHistory[0])
        for (i in 1 until locationHistory.size) {
            smoothedLocation.latitude += (locationHistory[i].latitude - locationHistory[i - 1].latitude) / locationHistory.size
            smoothedLocation.longitude += (locationHistory[i].longitude - locationHistory[i - 1].longitude) / locationHistory.size
        }
        return smoothedLocation
    }

    private fun setLocationData(location: Location?) {
        location?.let { loc ->
            filterLocation(loc)?.let {
                addLocationToHistory(it)
                val smoothLocation = getSmoothedLocation()
                if (smoothLocation != null) {
                    value = smoothLocation
                }
            }
        }
    }
}


internal fun updateRoutePolyline(route: List<LatLng>, marker: Marker?, polyline: Polyline?) {
    if (route.isEmpty() || marker == null || polyline == null) return

    var index = 0
    var minDistance = Float.MAX_VALUE
    for (i in route.indices) {
        val distance = marker.position.toLocation().distanceTo(route[i].toLocation())
        if (distance < minDistance) {
            index = i
            minDistance = distance
        }
    }

    if (index == route.size - 1) {
        polyline.remove()
        return
    }

    val nextPoint = route[index + 1].toLocation()
    val distanceToNextPoint = marker.position.toLocation().distanceTo(nextPoint).toDouble()
    val remainingDistance = route.slice(index..route.lastIndex)
        .foldIndexed(0.0) { i, acc, p ->
            if (i == 0) acc else acc + p.toLocation().distanceTo(route[i - 1].toLocation())
        } - distanceToNextPoint
    if (remainingDistance <= 0) {
        polyline.remove()
        return
    }
    val path = PolylineOptions()
        .color(Color.BLUE)
        .width(10f)
        .add(marker.position)
    var remainingDistanceOnPolyline = 0.0
    for (i in index until route.size - 1) {
        val p1 = route[i]
        val p2 = route[i + 1]
        val distance = p1.toLocation().distanceTo(p2.toLocation()).toDouble()
        if (remainingDistanceOnPolyline + distance >= remainingDistance) {
            val fraction = (remainingDistance - remainingDistanceOnPolyline) / distance
            val point = LatLng(
                p1.latitude + fraction * (p2.latitude - p1.latitude),
                p1.longitude + fraction * (p2.longitude - p1.longitude)
            )
            path.add(point)
            break
        } else {
            path.add(p1)
            remainingDistanceOnPolyline += distance
        }
    }

    polyline.points = path.points
}

fun LatLng.toLocation(): Location {
    val location = Location("")
    location.longitude = this.longitude
    location.latitude = this.latitude
    return location
}

fun Location.toLatLng(): LatLng = LatLng(
    this.latitude,
    this.longitude
)