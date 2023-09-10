package com.example.livetracking.ui.page.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.entity.PlaceEntity
import com.example.livetracking.domain.model.LocationData
import com.example.livetracking.repository.GoogleRepository
import com.example.livetracking.utils.DefaultLocationClient
import com.example.livetracking.utils.getTodayTimeStamp
import com.example.livetracking.utils.toLatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewModelSearch @Inject constructor(
    private val googleRepository: GoogleRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _searchResultStateUI = MutableStateFlow(SearchStateUI())
    val searchResultStateUI = _searchResultStateUI.asStateFlow()

    private val location = MutableStateFlow(LocationData())
    private val locationClient = DefaultLocationClient(context, 5000)

    init {
        getHistoriesPlace()
        locationClient.getLocationUpdates().onEach {
            location.emit(LocationData(it.latitude, it.longitude))
            Timber.tag("LOCATION_SEARCH").e(it.toLatLng().toString())
        }.launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    internal fun getCompleteLocation(queries: String) = viewModelScope.launch {
        googleRepository.autoCompleteLocation(
            queries = queries,
            myLoc = location.value
        )
            .debounce(800L)
            .onEach {
                _searchResultStateUI.emit(
                    when (it) {
                        is DataState.onData -> {
                            SearchStateUI(
                                data = it.data.map { prediction ->
                                    val distance =
                                        (prediction.distanceMeters ?: 0).toDouble() / 1000
                                    val distanceString = if (distance >= 1) {
                                        String.format("%.1f KM", distance)
                                    } else {
                                        String.format("%.0f M", distance * 1000)
                                    }
                                    SearchResultState(
                                        distanceMeters = distanceString,
                                        fullAddress = prediction.getFullText(null).toString(),
                                        placeId = prediction.placeId,
                                        placeTypes = prediction.placeTypes,
                                        primaryText = prediction.getPrimaryText(null).toString(),
                                        secondaryText = prediction.getSecondaryText(null)
                                            .toString(),
                                    )
                                }
                            )
                        }

                        is DataState.onFailure -> {
                            SearchStateUI(
                                error = true,
                                errMsg = it.error_message
                            )
                        }

                        DataState.onLoading -> {
                            SearchStateUI(
                                loading = true
                            )
                        }
                    }
                )
            }.collect()
    }

    internal fun saveHistoryPlace(
        namePlace: String,
        address: String,
        distance: String,
        placeId: String,
    ) = viewModelScope.launch {
        googleRepository.savePlace(
            PlaceEntity(
                namePlace = namePlace,
                address = address,
                distance = distance,
                placeId = placeId,
                createdAt = getTodayTimeStamp()
            )
        ).collect()
    }

    private fun getHistoriesPlace() = viewModelScope.launch {
        googleRepository.getHistoriesPlace().onEach {
            _searchResultStateUI.emit(
                when (it) {
                    is DataState.onData -> {
                        SearchStateUI(
                            data = it.data.map { data ->
                                SearchResultState(
                                    distanceMeters = data.distance,
                                    fullAddress = data.address,
                                    primaryText = data.namePlace,
                                    placeId = data.placeId,
                                    isHistory = true
                                )
                            }
                        )
                    }

                    is DataState.onFailure -> {
                        SearchStateUI(
                            error = true,
                            errMsg = it.error_message
                        )
                    }

                    DataState.onLoading -> {
                        SearchStateUI(
                            loading = true
                        )
                    }
                }
            )
        }.collect()
    }
}