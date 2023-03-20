package com.example.livetracking.utils

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.example.livetracking.domain.model.LocationData
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority

class LocationUtils(val context: Context, val DURATION_FOR_CHANGE_LOCATION: Long = 15000) :
    LiveData<LocationData>() {

    private var lastUpdateTime: Long = 0
    private var lastLocation: Location? = null


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

    override fun onInactive() {
        super.onInactive()
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
        accuracyThreshold: Float = 50.0f,
        maxElapsedMs: Long = 10000L
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

    private fun setLocationData(location: Location?) {
        location?.let { loc ->
            filterLocation(loc)?.let { loc2 ->
                value = LocationData(loc2.latitude, loc2.longitude)
            }
        }
    }
}
