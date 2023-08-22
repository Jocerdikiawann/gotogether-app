package com.example.livetracking.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.livetracking.BuildConfig
import com.example.livetracking.R
import com.example.livetracking.data.local.room.TokenDao
import com.example.livetracking.data.local.room.UserDao
import com.example.livetracking.data.remote.services.CourierService
import com.example.livetracking.domain.model.LocationData
import com.example.livetracking.domain.model.request.LocationRequest
import com.example.livetracking.repository.design.AuthRepository
import com.example.livetracking.utils.DefaultLocationClient
import com.example.livetracking.utils.LocationClient
import com.gojek.courier.callback.SendMessageCallback
import com.gojek.mqtt.client.MqttClient
import com.gojek.mqtt.model.KeepAlive
import com.gojek.mqtt.model.MqttConnectOptions
import com.gojek.mqtt.model.ServerUri
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var userRepository: AuthRepository

    @Inject
    lateinit var tokenDao: TokenDao

    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var mqttClient: MqttClient

    @Inject
    lateinit var courierService: CourierService

    private val TAG = "SERVICE"

    private lateinit var notificationManager: NotificationManager

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val notificationID = 1
    private val notificationChannel = "location_notify"
    private val mutableOfLocation: MutableStateFlow<LocationData> = MutableStateFlow(LocationData())
    private val mqttConnectionOptions: MqttConnectOptions =
        MqttConnectOptions.Builder()
            .serverUris(listOf(ServerUri("broker.hivemq.com", 8884, "wss")))
            .clientId(BuildConfig.BROKER_CLIENT_ID)
            .userName(BuildConfig.BROKER_USERNAME)
            .password(BuildConfig.BROKER_PASSWORD)
            .cleanSession(false)
            .keepAlive(KeepAlive(timeSeconds = 60))
            .build()

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private suspend fun sendLocation() {
        try {
            val gson = Gson()
            mutableOfLocation.onEach {
                courierService.publish(
                    "topic/location",
                    message = LocationRequest(
                        id = 1,
                        data = gson.toJson(
                            LocationData(
                                lat = it.lat,
                                lng = it.lng
                            )
                        )
                    ),
                    callback = object : SendMessageCallback {
                        override fun onMessageSendFailure(error: Throwable) {
                            Timber.tag(TAG).e("onMessageSendFailure $error")
                        }

                        override fun onMessageSendSuccess() {
                            Timber.tag(TAG).i("onMessageSendSuccess")
                        }

                        override fun onMessageSendTrigger() {
                            Timber.tag(TAG).i("onMessageSendTrigger")
                        }

                        override fun onMessageWrittenOnSocket() {
                            Timber.tag(TAG).i("onMessageWrittenOnSocket")
                        }

                    }
                )
                Timber.tag("DISINI").e("Lat : ${it.lat} Long:${it.lng}")
            }.collect()
        } catch (e: Exception) {
            Timber.tag(TAG).d(e)
        }
    }

    override fun onCreate() {
        super.onCreate()
        DefaultLocationClient(applicationContext, 500).getLocationUpdates().onEach {
            mutableOfLocation.emit(LocationData(it.latitude, it.longitude))
        }.launchIn(serviceScope)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        mqttClient.connect(mqttConnectionOptions)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        val name = notificationChannel
        val descriptionText = "notification_cexup"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            notificationChannel,
            name,
            importance
        ).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun start() {
        try {
            val appInfo = applicationContext.applicationInfo
            val stringId = appInfo.labelRes
            val appName =
                if (stringId == 0) appInfo.nonLocalizedLabel.toString() else applicationContext.getString(
                    stringId
                )
            val appIcon = appInfo.icon

            val notificationIntent = Intent()
            notificationIntent.setClassName(this, packageName)
            val pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )

            val intentStop = Intent(this, LocationService::class.java).apply {
                action = ACTION_STOP
            }
            val pendingIntentStop =
                PendingIntent.getService(this, 0, intentStop, PendingIntent.FLAG_IMMUTABLE)


            val builder = NotificationCompat
                .Builder(this, notificationChannel)
                .setContentTitle(appName)
                .setContentText("Location services are running in the background.")
                .setSmallIcon(appIcon)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOngoing(true)
                .addAction(
                    R.drawable.ic_stop,
                    "Stop",
                    pendingIntentStop
                )
            startForeground(notificationID, builder.build())
            serviceScope.launch { sendLocation() }
        } catch (e: Exception) {
            Timber.tag(TAG).d(e)
        }
    }


    private fun stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
        stopSelf()
        notificationManager.cancel(notificationID)
        mqttClient.disconnect()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        mqttClient.disconnect()
    }

}