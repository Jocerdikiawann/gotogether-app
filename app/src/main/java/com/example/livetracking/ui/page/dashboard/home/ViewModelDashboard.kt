package com.example.livetracking.ui.page.dashboard.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.repository.design.GoogleRepository
import com.example.livetracking.utils.LocationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelDashboard @Inject constructor(
    private val googleRepository: GoogleRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {


    private var _havePermission = MutableLiveData<LocationStateUI>(LocationStateUI())
    val havePermission get() = _havePermission

    private var _addressStateUI = MutableLiveData<AddressStateUI>(AddressStateUI())
    val addressStateUI get() = _addressStateUI

    private val locationUtils = LocationUtils(context)

    init {
        havePermission()
        startLocationUpdate()
    }

    fun turnOnGps(resultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        locationUtils.turnOnGPS(resultLauncher)
    }

    internal fun startLocationUpdate() {
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

    fun getAddress(lat: Double, lng: Double) = viewModelScope.launch {
        googleRepository.geocodingLocation("$lat,$lng").collect {
            _addressStateUI.postValue(
                when (it) {
                    is DataState.onData -> {
                        val addressComponent =
                            it.data.results.firstOrNull()?.address_components ?: listOf()
                        var addressFirst = ""
                        var addressSecond = ""
                        for (component in addressComponent) {
                            when {
                                component.types.contains("administrative_area_level_2") -> {
                                    addressFirst = component.long_name
                                }
                                component.types.contains("administrative_area_level_1") -> {
                                    addressSecond = component.long_name
                                }
                            }
                        }
                        AddressStateUI(
                            addressFirst = addressFirst,
                            addressSecond = addressSecond
                        )
                    }
                    is DataState.onFailure -> {
                        AddressStateUI(
                            error = true,
                            errMsg = it.error_message
                        )
                    }
                    DataState.onLoading -> AddressStateUI(
                        loading = true
                    )
                }
            )
        }
    }
}