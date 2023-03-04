package com.example.livetracking.ui.page.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.LocationData
import com.example.livetracking.domain.model.fromJson
import com.example.livetracking.repository.design.GoogleRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelSearch @Inject constructor(
    private val googleRepository: GoogleRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val searchArgs = Search.SearchArgs(savedStateHandle)

    private var _searchResultStateUI = MutableLiveData<SearchStateUI>()
    val searchResultStateUI get() = _searchResultStateUI

    init {

    }

    internal fun getCompleteLocation(queries: String) = viewModelScope.launch {
        val gson = Gson()
        val myLoc = gson.fromJson<LocationData>(searchArgs.location)
        googleRepository.autoCompleteLocation(queries = queries, myLoc = myLoc).onEach {
            _searchResultStateUI.postValue(
                when (it) {
                    is DataState.onData -> {
                        SearchStateUI(
                            data = it.data.map { prediction ->
                                SearchResultState(
                                    distanceMeters = "${prediction.distanceMeters ?: 0} Meters",
                                    fullAddress = prediction.getFullText(null).toString(),
                                    placeId = prediction.placeId,
                                    placeTypes = prediction.placeTypes,
                                    primaryText = prediction.getPrimaryText(null).toString(),
                                    secondaryText = prediction.getSecondaryText(null).toString(),
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