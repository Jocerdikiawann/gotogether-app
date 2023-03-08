package com.example.livetracking.ui.page.direction

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.LocationData
import com.example.livetracking.domain.utils.RouteTravelModes
import com.example.livetracking.repository.design.GoogleRepository
import com.example.livetracking.utils.LocationUtils
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
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
    private val locationUtils = LocationUtils(context)

    private var _destinationStateUI = MutableLiveData<DestinationStateUI>(DestinationStateUI())
    val destinationStateUI get() = _destinationStateUI

    private var _locationStateUI = MutableLiveData<LocationStateUI>(LocationStateUI())
    val locationStateUI get() = _locationStateUI

    private var _directionStateUI = MutableLiveData<DirectionStateUI>(DirectionStateUI())
    val directionStateUI get() = _directionStateUI

    private var _gpsIsOn = MutableLiveData<Boolean?>(null)
    val gpsIsOn get() = _gpsIsOn

    private val locationObserver = Observer<LocationData> {
        _locationStateUI.postValue(
            LocationStateUI(it.lat, it.lng)
        )
    }

    init {
        locationUtils.observeForever(locationObserver)
        getDetailPlace()
    }

    override fun onCleared() {
        super.onCleared()
        locationUtils.removeObserver(locationObserver)
    }


    internal fun startLocationUpdate() {
        locationUtils.startLocationUpdate()
    }

    private fun getDirectionRoutes(destination: LatLng) =
        viewModelScope.launch {
            googleRepository.getRoutesDirection(
                origin = LatLng(
                    locationStateUI.value?.lat ?: 0.0,
                    locationStateUI.value?.lng ?: 0.0
                ),
                destination = destination,
                travelModes = RouteTravelModes.TWO_WHEELER,
            ).onEach {
                _directionStateUI.postValue(
                    when (it) {
                        is DataState.onData -> {
                            DirectionStateUI(
                                data = it.data.routes?.map { route ->
                                    DirectionData(
                                        duration = route.duration,
                                        route = PolyUtil.decode(route.polyline.encodedPolyline),
                                        distance = if (route.distanceMeters >= 1000)
                                            "${route.distanceMeters / 1000} KM"
                                        else
                                            "${route.distanceMeters} M",
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
                        it.data.latLng?.let { it1 -> getDirectionRoutes(it1) }
                        DestinationStateUI(
                            destination = it.data.latLng ?: LatLng(0.0, 0.0),
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