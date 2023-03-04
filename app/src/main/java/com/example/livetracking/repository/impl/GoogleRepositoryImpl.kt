package com.example.livetracking.repository.impl

import com.example.livetracking.data.coroutines.DispatcherProvider
import com.example.livetracking.data.remote.design.GoogleDataSource
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.LocationData
import com.example.livetracking.domain.model.response.GeocodingResponse
import com.example.livetracking.domain.model.response.GoogleMapsInfoModel
import com.example.livetracking.domain.utils.TravelModes
import com.example.livetracking.repository.design.GoogleRepository
import com.example.livetracking.utils.CalculationBoundsUtils
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.ktx.api.net.awaitFetchPlace
import com.google.android.libraries.places.ktx.api.net.awaitFindAutocompletePredictions
import com.google.android.libraries.places.ktx.api.net.findAutocompletePredictionsRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class GoogleRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val googleDataSource: GoogleDataSource,
    private val placesClient: PlacesClient,
) : GoogleRepository {
    override suspend fun getDirection(
        origin: String,
        mode: TravelModes,
        destination: String
    ): Flow<DataState<GoogleMapsInfoModel>> = flow {
        emit(DataState.onLoading)
        try {
            when (val result = googleDataSource.getDirection(origin, mode, destination)) {
                is DataState.onData -> {
                    emit(DataState.onData(result.data))
                }
                is DataState.onFailure -> emit(DataState.onFailure(result.error_message))
                DataState.onLoading -> emit(DataState.onLoading)
            }
        } catch (e: HttpException) {
            emit(DataState.onFailure("Something when wrong"))
        } catch (e: IOException) {
            emit(DataState.onFailure("No internet connection"))
        }
    }

    override suspend fun geocodingLocation(latlng: String): Flow<DataState<GeocodingResponse>> =
        flow {
            emit(DataState.onLoading)
            try {
                when (val result = googleDataSource.geocodingLocation(latlng)) {
                    is DataState.onData -> {
                        emit(DataState.onData(result.data))
                    }
                    is DataState.onFailure -> {
                        emit(DataState.onFailure(result.error_message))
                    }
                    DataState.onLoading -> emit(DataState.onLoading)
                }
            } catch (e: HttpException) {
                emit(DataState.onFailure("Something when wrong"))
            } catch (e: IOException) {
                emit(DataState.onFailure("No internet connection"))
            }
        }

    override suspend fun autoCompleteLocation(
        queries: String,
        myLoc: LocationData
    ): Flow<DataState<List<AutocompletePrediction>>> =
        flow<DataState<List<AutocompletePrediction>>> {
            emit(DataState.onLoading)
            val token = AutocompleteSessionToken.newInstance()
            val bounds = RectangularBounds.newInstance(
                CalculationBoundsUtils(myLoc.lat, myLoc.lng, -15000, -15000),
                CalculationBoundsUtils(myLoc.lat, myLoc.lng, 15000, 15000)
            )
            val request = findAutocompletePredictionsRequest {
                locationBias = bounds
                typesFilter = listOf()
                sessionToken = token
                origin = LatLng(myLoc.lat, myLoc.lng)
                query = queries
            }

            try {
                val response =
                    placesClient.awaitFindAutocompletePredictions(request)

                emit(DataState.onData(response.autocompletePredictions))
            } catch (e: Exception) {
                if (e is ApiException) {
                    emit(DataState.onFailure(e.message ?: "internal error"))
                }
            }
        }.flowOn(dispatcherProvider.io())

    override suspend fun fetchPlaces(placeId: String): Flow<DataState<Place>> =
        flow<DataState<Place>> {
            emit(DataState.onLoading)
            val request =
                FetchPlaceRequest.newInstance(
                    placeId,
                    listOf(
                        Place.Field.ID, Place.Field.NAME,
                        Place.Field.LAT_LNG, Place.Field.ADDRESS
                    ),
                )
            try {
                val response = placesClient.awaitFetchPlace(request = request)
                emit(DataState.onData(response.place))
            } catch (e: Exception) {
                if (e is ApiException) {
                    emit(DataState.onFailure(e.message ?: "internal error"))
                }
            }

        }
}