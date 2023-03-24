package com.example.livetracking.ui.page.direction

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.GyroData
import com.example.livetracking.domain.utils.RouteTravelModes
import com.example.livetracking.repository.design.GoogleRepository
import com.example.livetracking.utils.GyroscopeUtils
import com.example.livetracking.utils.LocationUtils
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelDirection @Inject constructor(
    private val googleRepository: GoogleRepository,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext context: Context
) : ViewModel() {
    private val destinationArgs = Direction.DirectionArgs(savedStateHandle)
    private val locationUtils = LocationUtils(context, DURATION_FOR_CHANGE_LOCATION = 1)
    private val gyroscopeUtils = GyroscopeUtils(context)

    private var _destinationStateUI = MutableLiveData<DestinationStateUI>(DestinationStateUI())
    val destinationStateUI get() = _destinationStateUI

    private var _locationStateUI = MutableLiveData<LocationStateUI>(LocationStateUI())
    val locationStateUI get() = _locationStateUI

    private var _directionStateUI = MutableLiveData<DirectionStateUI>(DirectionStateUI())
    val directionStateUI get() = _directionStateUI

    private var _gyroscopeStateUI = MutableLiveData<GyroData>(GyroData())
    val gyroScopeStateUI get() = _gyroscopeStateUI

    private var _gpsIsOn = MutableLiveData<Boolean?>(null)
    val gpsIsOn get() = _gpsIsOn

    private val locationObserver = Observer<Location> { currentLocation ->
        _locationStateUI.postValue(
            LocationStateUI(LatLng(currentLocation.latitude, currentLocation.longitude))
        )
    }

    private val gyroscopeObserver = Observer<GyroData> {
        _gyroscopeStateUI.postValue(
            GyroData(
                pitch = it.pitch,
                roll = it.roll,
                azimuth = it.azimuth
            )
        )
    }

    init {
        getDetailPlace()
        locationUtils.observeForever(locationObserver)
        gyroscopeUtils.observeForever(gyroscopeObserver)
    }

    override fun onCleared() {
        super.onCleared()
        locationUtils.removeObserver(locationObserver)
    }

    private fun getDirectionRoutes(destination: LatLng) =
        viewModelScope.launch {
            googleRepository.getRoutesDirection(
                origin = locationStateUI.value?.myLoc ?: LatLng(0.0,0.0),
                destination = destination,
                travelModes = RouteTravelModes.TWO_WHEELER,
            ).onEach {
                _directionStateUI.postValue(
                    when (it) {
                        is DataState.onData -> {
                            DirectionStateUI(
                                data = it.data.routes?.map { route ->
                                    val time = route.duration.replace("s", "").toIntOrNull()
                                    val duration = when {
                                        time == null -> {
                                            "0 Sec"
                                        }
                                        (time >= 3600) -> {
                                            "${time / 3600} Hour ${(time % 3600) / 60} Min"
                                        }
                                        time >= 60 -> {
                                            "${time / 60} Min ${(time % 60)} Sec"
                                        }
                                        else -> {
                                            "$time Sec"
                                        }
                                    }
                                    val distance = if (route.distanceMeters >= 1000)
                                        "${route.distanceMeters / 1000} KM"
                                    else
                                        "${route.distanceMeters} M"
                                    DirectionData(
                                        estimate = "$distance - $duration",
                                        route = PolyUtil.decode(route.polyline.encodedPolyline),
                                    )
                                } ?: listOf()
                            )
                        }
                        is DataState.onFailure -> {
                            DirectionStateUI(
                                error = true,
                                errMsg = it.error_message
                            )
                        }
                        DataState.onLoading -> {
                            DirectionStateUI(
                                loading = true
                            )
                        }
                    }
                )
            }.collect()
        }

    private fun getDetailPlace() = viewModelScope.launch {
        googleRepository.getDetailPlace(destinationArgs.placeId).onEach {
            _destinationStateUI.postValue(
                when (it) {
                    is DataState.onData -> {
                        it.data.place.latLng?.let { it1 -> getDirectionRoutes(it1) }
                        DestinationStateUI(
                            destination = it.data.place.latLng ?: LatLng(0.0, 0.0),
                            title = it.data.place.name ?: "",
                            address = it.data.place.address ?: "",
                            image = it.data.photoBitmap
                        )
                    }
                    is DataState.onFailure -> {
                        DestinationStateUI(
                            error = true,
                            errMsg = it.error_message
                        )
                    }
                    DataState.onLoading -> {
                        DestinationStateUI(
                            loading = true
                        )
                    }
                }
            )
        }.collect()
    }


}