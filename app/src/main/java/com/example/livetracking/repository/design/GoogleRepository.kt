package com.example.livetracking.repository.design

import com.example.livetracking.data.utils.DataState
import com.example.livetracking.domain.model.GoogleMapsInfoModel
import com.example.livetracking.domain.utils.TravelModes
import kotlinx.coroutines.flow.Flow

interface GoogleRepository {
    suspend fun getDirection(
        origin: String,
        mode: TravelModes,
        destination: String
    ): Flow<DataState<GoogleMapsInfoModel>>
}