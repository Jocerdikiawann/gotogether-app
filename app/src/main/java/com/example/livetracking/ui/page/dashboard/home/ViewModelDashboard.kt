package com.example.livetracking.ui.page.dashboard.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.GyroData
import com.example.livetracking.repository.design.GoogleRepository
import com.example.livetracking.utils.DefaultLocationClient
import com.example.livetracking.utils.GyroscopeUtils
import com.example.livetracking.utils.toLatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ViewModelDashboard @Inject constructor(
    private val googleRepository: GoogleRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val locationClient = DefaultLocationClient(context,10000)

    private val _havePermission = MutableStateFlow(LocationStateUI())
    val havePermission = _havePermission.asStateFlow()

    private val _addressStateUI = MutableStateFlow(AddressStateUI())
    val addressStateUI = _addressStateUI.asStateFlow()

    private val _dashboardStateUI = MutableStateFlow(DashboardStateUI())
    val dashboardStateUI = _dashboardStateUI.asStateFlow()

    private val _gyroScopeStateUI = MutableStateFlow(GyroData())
    val gyroScopeStateUI = _gyroScopeStateUI.asStateFlow()

    private val gyroscopeUtils = GyroscopeUtils(context)

    private val gyroScopeObserver = Observer<GyroData> {
        viewModelScope.launch {
            _gyroScopeStateUI.emit(GyroData(pitch = it.pitch, roll = it.roll, azimuth = it.azimuth))
        }
    }

    init {
        gyroscopeUtils.observeForever(gyroScopeObserver)
        havePermission()
        getLocationUpdates()
    }

    fun turnOnGps(resultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        locationClient.turnOnGps(resultLauncher)
    }

    fun getLocationUpdates() = viewModelScope.launch {
        locationClient.getLocationUpdates()
            .catch { e -> e.printStackTrace() }
            .onEach {
                _dashboardStateUI.emit(DashboardStateUI(lat = it.latitude, lng = it.longitude))
            }
            .collect()
    }

    fun havePermission() = viewModelScope.launch {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        _havePermission.emit(
            LocationStateUI(
                isGpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER),
                permission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
        )
    }

    fun getAddress(lat: Double, lng: Double) = viewModelScope.launch {
        googleRepository.geocodingLocation("$lat,$lng").collect {
            _addressStateUI.emit(
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