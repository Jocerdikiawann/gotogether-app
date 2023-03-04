package com.example.livetracking.repository.design

import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.LocationData
import com.example.livetracking.domain.model.response.GeocodingResponse
import com.example.livetracking.domain.model.response.GoogleMapsInfoModel
import com.example.livetracking.domain.utils.TravelModes
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.flow.Flow

interface GoogleRepository {
    suspend fun getDirection(
        origin: String,
        mode: TravelModes,
        destination: String
    ): Flow<DataState<GoogleMapsInfoModel>>

    suspend fun geocodingLocation(
        latlng: String
    ): Flow<DataState<GeocodingResponse>>

    suspend fun autoCompleteLocation(
        queries: String,
        myLoc: LocationData
    ): Flow<DataState<List<AutocompletePrediction>>>

    suspend fun fetchPlaces(
        placeId: String,
    ): Flow<DataState<Place>>
}