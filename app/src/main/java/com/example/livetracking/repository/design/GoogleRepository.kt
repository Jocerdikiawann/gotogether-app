package com.example.livetracking.repository.design

import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.entity.PlaceEntity
import com.example.livetracking.domain.model.LocationData
import com.example.livetracking.domain.model.PlaceData
import com.example.livetracking.domain.model.response.GeocodingResponse
import com.example.livetracking.domain.model.response.GoogleMapsInfoModel
import com.example.livetracking.domain.model.response.RoutesResponse
import com.example.livetracking.domain.utils.RouteTravelModes
import com.example.livetracking.domain.utils.TravelModes
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import kotlinx.coroutines.flow.Flow

interface GoogleRepository {
    suspend fun getDirection(
        origin: String,
        mode: TravelModes,
        destination: String
    ): Flow<DataState<GoogleMapsInfoModel>>

    suspend fun getRoutesDirection(
        origin: LatLng,
        destination: LatLng,
        travelModes: RouteTravelModes,
    ): Flow<DataState<RoutesResponse>>

    suspend fun geocodingLocation(
        latlng: String
    ): Flow<DataState<GeocodingResponse>>

    suspend fun autoCompleteLocation(
        queries: String,
        myLoc: LocationData
    ): Flow<DataState<List<AutocompletePrediction>>>

    suspend fun getDetailPlace(
        placeId: String,
    ): Flow<DataState<PlaceData>>

    suspend fun savePlace(
        placeEntity: PlaceEntity
    ): Flow<Boolean>

    suspend fun getHistoriesPlace() : Flow<DataState<List<PlaceEntity>>>
}