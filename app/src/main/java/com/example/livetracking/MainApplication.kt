package com.example.livetracking

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.MapsInitializer
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        try {
            MapsInitializer.initialize(this)
            if (!Places.isInitialized()) {
                Places.initialize(this, BuildConfig.MAPS_API_KEYS)
            }
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

        super.onCreate()
    }
}