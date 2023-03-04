package com.example.livetracking.ui.page.direction

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.domain.model.LocationData
import com.example.livetracking.repository.design.GoogleRepository
import com.example.livetracking.utils.LocationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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

    private val locationObserver = Observer<LocationData> {

    }

    private val _directionStateUI = MutableLiveData<DirectionStateUI>(DirectionStateUI())
    val directionStateUI get() = _directionStateUI

    init {
        locationUtils.observeForever(locationObserver)
    }

    override fun onCleared() {
        super.onCleared()
        locationUtils.removeObserver(locationObserver)
    }

    internal fun startLocationUpdate() {
        locationUtils.startLocationUpdate()
    }

    private fun getDetailPlace() = viewModelScope.launch {
        googleRepository.fetchPlaces(destinationArgs.destination)
    }


}