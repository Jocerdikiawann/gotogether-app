package com.example.livetracking.data.remote.services

import com.example.livetracking.domain.model.request.LocationRequest
import com.gojek.courier.QoS
import com.gojek.courier.annotation.Callback
import com.gojek.courier.annotation.Data
import com.gojek.courier.annotation.Path
import com.gojek.courier.annotation.Send
import com.gojek.courier.callback.SendMessageCallback

interface CourierService {
    @Send(topic = "{topic}", qos = QoS.ONE)
    fun publish(
        @Path("topic") topic: String,
        @Data message: LocationRequest,
        @Callback callback: SendMessageCallback
    )
}