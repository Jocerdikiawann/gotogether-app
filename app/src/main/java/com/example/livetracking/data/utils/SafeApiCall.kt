package com.example.livetracking.data.utils

import androidx.annotation.Keep
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(call: suspend () -> Response<T>): DataState<T> {

    try {
        val response = call.invoke()
        if (response.code() in 200..209) {
            return DataState.onData((response.body()) as T)
        }
        if (response.code() in 400..500) {
            val gson = Gson()
            val json = response.errorBody()?.string()
            val contentType = response.raw().body?.contentType()
            if (contentType != null && contentType.subtype == "html") {
                return DataState.onFailure("Internal server error")
            }
            if (json.isNullOrBlank()) {
                return DataState.onFailure("Failed to authenticate")
            }
            val error = gson.fromJson(json, ErrorBody::class.java)
            return DataState.onFailure(error_message = error.error_message.toString(), message = error.message.toString())
        }
        return DataState.onFailure(response.message())
    } catch (e: Exception) {
        return when (e) {
            is IOException -> {
                DataState.onFailure(e.message ?: "")
            }
            is ConnectException -> {
                DataState.onFailure(CONNECT_EXCEPTION)
            }
            is UnknownHostException -> {
                DataState.onFailure(UNKNOWN_HOST_EXCEPTION)
            }
            is SocketTimeoutException -> {
                DataState.onFailure(SOCKET_TIME_OUT_EXCEPTION)
            }
            is HttpException -> {
                DataState.onFailure(UNKNOWN_NETWORK_EXCEPTION)
            }
            else -> {
                DataState.onFailure(e.message ?: "")
            }
        }
    }
}

@Keep
data class ErrorBody(
    var error_message: String? = "",
    var message:String?=""
)