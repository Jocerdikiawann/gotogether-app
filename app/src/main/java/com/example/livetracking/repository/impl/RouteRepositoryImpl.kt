package com.example.livetracking.repository.impl

import com.example.livetracking.RouteGrpc
import com.example.livetracking.RouteGrpcKt
import com.example.livetracking.RouteProto
import com.example.livetracking.RouteProto.LocationRequest
import com.example.livetracking.RouteProto.LocationResponse
import com.example.livetracking.data.coroutines.DispatcherProvider
import com.example.livetracking.data.local.room.TokenDao
import com.example.livetracking.data.local.room.UserDao
import com.example.livetracking.data.utils.DataState
import com.example.livetracking.destintationAndPolylineRequest
import com.example.livetracking.locationRequest
import com.example.livetracking.point
import com.example.livetracking.repository.design.RouteRepository
import io.grpc.ManagedChannel
import io.grpc.Metadata
import io.grpc.stub.MetadataUtils
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RouteRepositoryImpl(
    private val channel: ManagedChannel,
    private val dispatcherProvider: DispatcherProvider,
    private val userDao: UserDao,
    private val tokenDao: TokenDao
) : RouteRepository {
    override suspend fun sendLocation(
        location: Flow<LocationRequest>,
    ): Flow<DataState<LocationResponse>> = flow {
       emit(DataState.onLoading)
    }.flowOn(dispatcherProvider.io())

    override suspend fun sendDestinationAndPolyline(
        latitudeDestination: Double,
        longitudeDestination: Double,
        encodedRoute: String
    ): Flow<DataState<RouteProto.DestintationAndPolylineResponse>> = flow {
        emit(DataState.onLoading)
        try {
            val header = Metadata()
            val key = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val token = tokenDao.getToken()?.token.orEmpty()
            val googleId = userDao.getUser()?.googleId.orEmpty()
            val stub = RouteGrpcKt.RouteCoroutineStub(channel)
            header.put(key, token)
            val request = destintationAndPolylineRequest {
                this.destination = point {
                    this.latitude = latitudeDestination
                    this.longitude = longitudeDestination
                }
                this.encodedRoute = encodedRoute
                this.googleId = googleId
            }
            val response = stub.sendDestinationAndPolyline(request, header)
            if (response.success) {
                emit(DataState.onData(response))
            } else {
                emit(DataState.onFailure(response.message))
            }
        } catch (e: Exception) {
            emit(DataState.onFailure(e.message ?: "Internal Error"))
        } finally {
            channel.shutdownNow()
        }
    }
}