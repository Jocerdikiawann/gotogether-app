package com.example.livetracking.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.example.livetracking.R
import com.example.livetracking.RouteGrpc
import com.example.livetracking.RouteProto
import com.example.livetracking.data.local.room.TokenDao
import com.example.livetracking.data.local.room.UserDao
import com.example.livetracking.data.remote.AppData
import com.example.livetracking.domain.model.LocationData
import com.example.livetracking.locationRequest
import com.example.livetracking.point
import com.example.livetracking.repository.design.AuthRepository
import com.example.livetracking.utils.LocationUtils
import dagger.hilt.android.AndroidEntryPoint
import io.grpc.ManagedChannel
import io.grpc.Metadata
import io.grpc.stub.MetadataUtils
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var userRepository: AuthRepository

    @Inject
    lateinit var tokenDao: TokenDao

    @Inject
    lateinit var userDao:UserDao

    private lateinit var channel: ManagedChannel
    private lateinit var notificationManager: NotificationManager
    private lateinit var locationUtils: LocationUtils
    private lateinit var locationObserver: Observer<Location>

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val notificationID = 1
    private val notificationChannel = "location_notify"
    private val mutableOfLocation : MutableStateFlow<LocationData> = MutableStateFlow(LocationData())

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private suspend fun sendLocation() {
        try {
            channel = AppData.initChannel("192.168.1.2", 8888)
            val header = Metadata()
            val key =
                Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
            val googleId = userDao.getUser()?.googleId.orEmpty()
            val token = tokenDao.getToken()?.token.orEmpty()
            header.put(key, token)
            val stub = RouteGrpc.newStub(channel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(header))
            val observer =  stub.sendLocation(object : StreamObserver<RouteProto.LocationResponse> {
                override fun onNext(value: RouteProto.LocationResponse) {
                    Log.e("ON_NEXT", value.toString())
                }

                override fun onError(t: Throwable?) {
                    Log.e("ON_ERROR", t?.message.toString())
                }

                override fun onCompleted() {
                    channel.shutdown()
                }
            })
            mutableOfLocation.onEach {
                observer?.onNext(locationRequest {
                    this.point = point {
                        this.latitude = it.lat
                        this.longitude = it.lng
                    }
                    this.googleId = googleId
                    this.isFinish = false
                })
            }.collect()
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
        }
    }

    override fun onCreate() {
        super.onCreate()
        locationUtils = LocationUtils(applicationContext, 0)
        locationObserver = Observer<Location> {
            serviceScope.launch {
                mutableOfLocation.emit(LocationData(
                    lat = it.latitude,
                    lng = it.longitude
                ))
            }
        }
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
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

            locationUtils.observeForever(locationObserver)
            serviceScope.launch {
                sendLocation()
            }
            startForeground(notificationID, builder.build())
        } catch (e: Exception) {
            Log.e("EN", e.message.toString())
        }
    }


    private fun stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
        stopSelf()
        locationUtils.removeObserver(locationObserver)
        locationUtils.stopUpdateLocation()
        notificationManager.cancel(notificationID)
        channel.shutdown()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        locationUtils.removeObserver(locationObserver)
        locationUtils.stopUpdateLocation()
        channel.shutdown()
    }

}