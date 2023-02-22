package com.example.livetracking.ui.page.dashboard

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.repository.design.GoogleRepository
import com.example.livetracking.utils.LocationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelDashboard @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {


    private var _havePermission = MutableLiveData<LocationStateUI>(LocationStateUI())
    val havePermission get() = _havePermission

    private val locationUtils = LocationUtils(context)
    fun turnOnGps(activity: Activity) {
        locationUtils.turnOnGPS(activity)
    }

    fun startLocationUpdate() {
        locationUtils.startLocationUpdate()
    }

    fun havePermission() = viewModelScope.launch {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        _havePermission.postValue(
            LocationStateUI(
                isGpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER),
                permission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
        )
    }

    fun getLocation() = locationUtils
}