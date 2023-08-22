package com.example.livetracking.di

import android.content.Context
import com.example.livetracking.BuildConfig
import com.example.livetracking.data.coroutines.DefaultDispatcherProvider
import com.example.livetracking.data.coroutines.DispatcherProvider
import com.example.livetracking.data.local.room.AppDatabase
import com.example.livetracking.data.local.room.PlaceDao
import com.example.livetracking.data.local.room.TokenDao
import com.example.livetracking.data.local.room.UserDao
import com.example.livetracking.data.remote.AppData
import com.example.livetracking.data.remote.design.GoogleDataSource
import com.example.livetracking.data.remote.design.RoutesDataSource
import com.example.livetracking.data.remote.design.ShareTripDataSource
import com.example.livetracking.data.remote.services.CourierService
import com.example.livetracking.repository.design.AuthRepository
import com.example.livetracking.repository.design.GoogleRepository
import com.example.livetracking.repository.design.RouteRepository
import com.example.livetracking.repository.impl.AuthRepositoryImpl
import com.example.livetracking.repository.impl.GoogleRepositoryImpl
import com.example.livetracking.repository.impl.RouteRepositoryImpl
import com.gojek.chuckmqtt.external.MqttChuckConfig
import com.gojek.chuckmqtt.external.MqttChuckInterceptor
import com.gojek.chuckmqtt.external.Period
import com.gojek.courier.Courier
import com.gojek.courier.logging.ILogger
import com.gojek.courier.messageadapter.gson.GsonMessageAdapterFactory
import com.gojek.courier.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import com.gojek.mqtt.auth.Authenticator
import com.gojek.mqtt.client.MqttClient
import com.gojek.mqtt.client.config.ExperimentConfigs
import com.gojek.mqtt.client.config.PersistenceOptions
import com.gojek.mqtt.client.config.v3.MqttV3Configuration
import com.gojek.mqtt.client.factory.MqttClientFactory
import com.gojek.mqtt.event.EventHandler
import com.gojek.mqtt.event.MqttEvent
import com.gojek.mqtt.model.AdaptiveKeepAliveConfig
import com.gojek.mqtt.model.MqttConnectOptions
import com.gojek.workmanager.pingsender.WorkManagerPingSenderConfig
import com.gojek.workmanager.pingsender.WorkPingSenderFactory
import com.google.android.libraries.places.api.Places
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideCourierService(
        courier: Courier
    ): CourierService {
        return courier.create()
    }

    @Provides
    fun provideCourier(
        courierConfig: Courier.Configuration
    ): Courier {
        return Courier(courierConfig)
    }

    @Provides
    fun provideCourierConfiguration(
        mqttClient: MqttClient,
        courierLogger: ILogger
    ): Courier.Configuration {
        return Courier.Configuration(
            client = mqttClient,
            streamAdapterFactories = listOf(CoroutinesStreamAdapterFactory()),
            messageAdapterFactories = listOf(GsonMessageAdapterFactory()),
            logger = courierLogger,
        )
    }

    @Provides
    @Singleton
    fun provideMqttClient(
        @ApplicationContext context: Context,
        mqttConfiguration: MqttV3Configuration,
        eventHandler: EventHandler,
    ): MqttClient {
        val client = MqttClientFactory.create(
            context, mqttConfiguration
        )
        client.addEventHandler(eventHandler)
        return client
    }

    @Provides
    fun provideMqttV3Configuration(
        @ApplicationContext context: Context,
        logger: ILogger,
        authenticator: Authenticator,
    ): MqttV3Configuration {
        return MqttV3Configuration(
            logger = logger,
            authenticator = authenticator,
            mqttInterceptorList = listOf(MqttChuckInterceptor(context, MqttChuckConfig(retentionPeriod = Period.ONE_HOUR))),
            persistenceOptions = PersistenceOptions.PahoPersistenceOptions(100, false),
            pingSender = WorkPingSenderFactory.createMqttPingSender(
                context,
                WorkManagerPingSenderConfig(sendForcePing = true)
            ),
            experimentConfigs = ExperimentConfigs(
                adaptiveKeepAliveConfig = AdaptiveKeepAliveConfig(
                    lowerBoundMinutes = 1,
                    upperBoundMinutes = 9,
                    stepMinutes = 2,
                    optimalKeepAliveResetLimit = 10,
                    pingSender = WorkPingSenderFactory.createAdaptiveMqttPingSender(
                        context,
                        WorkManagerPingSenderConfig()
                    ),
                ),
                inactivityTimeoutSeconds = 45,
                activityCheckIntervalSeconds = 30,
                connectPacketTimeoutSeconds = 5,
                incomingMessagesTTLSecs = 60,
                incomingMessagesCleanupIntervalSecs = 10,
                maxInflightMessagesLimit = 1000,
            ),
        )
    }

    @Provides
    fun getLogger() = object : ILogger {
        override fun v(tag: String, msg: String) {
            Timber.tag("Courier").v(msg)
        }

        override fun v(tag: String, msg: String, tr: Throwable) {
            Timber.tag("Courier").v(tr, msg)
        }

        override fun d(tag: String, msg: String) {
            Timber.tag("Courier").d(msg)
        }

        override fun d(tag: String, msg: String, tr: Throwable) {
            Timber.tag("Courier").d(tr, msg)
        }

        override fun i(tag: String, msg: String) {
            Timber.tag("Courier").i(msg)
        }

        override fun i(tag: String, msg: String, tr: Throwable) {
            Timber.tag("Courier").i(tr, msg)
        }

        override fun w(tag: String, msg: String) {
            Timber.tag("Courier").w(msg)
        }

        override fun w(tag: String, msg: String, tr: Throwable) {
            Timber.tag("Courier").w(tr, msg)
        }

        override fun w(tag: String, tr: Throwable) {
            Timber.tag("Courier").d(tr)
        }

        override fun e(tag: String, msg: String) {
            Timber.tag("Courier").e(msg)
        }

        override fun e(tag: String, msg: String, tr: Throwable) {
            Timber.tag("Courier").e(tr, msg)
        }
    }

    @Provides
    fun eventHandler() = object : EventHandler {
        override fun onEvent(mqttEvent: MqttEvent) {
            Timber.tag("Courier").d("Received Events : $mqttEvent")
        }
    }

    @Provides
    fun authenticator() = object : Authenticator {
        override fun authenticate(
            connectOptions: MqttConnectOptions,
            forceRefresh: Boolean
        ): MqttConnectOptions {
            return connectOptions
        }

    }

    @Provides
    internal fun providesGoogleDataSource(): GoogleDataSource = AppData.googleDataSource(
        BuildConfig.BASE_URL_GOOGLE, BuildConfig.MAPS_API_KEYS
    )

    @Provides
    internal fun providesRoutesDataSource(): RoutesDataSource = AppData.routesDataSource(
        BuildConfig.MAPS_API_KEYS, BuildConfig.BASE_URL_ROUTES_API
    )

    @Provides
    internal fun provideShareTripDataSource(): ShareTripDataSource = AppData.clientShareTripSource(
        BuildConfig.BASE_URL_SHARE_TRIP
    )

    @Provides
    internal fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    internal fun provideLocalDatabase(
        @ApplicationContext ctx: Context
    ): AppDatabase = AppData.initDatabase(ctx)

    @Provides
    internal fun providePlaceDao(appDb: AppDatabase): PlaceDao = appDb.placeDao()

    @Provides
    internal fun provideUserDao(appDb: AppDatabase): UserDao = appDb.userDao()

    @Provides
    internal fun provideTokenDao(appDb: AppDatabase): TokenDao = appDb.tokenDao()


    @Provides
    internal fun provideGoogleRepository(
        dispatcherProvider: DispatcherProvider,
        googleDataSource: GoogleDataSource,
        routeDataSource: RoutesDataSource,
        placeDao: PlaceDao,
        @ApplicationContext context: Context,
    ): GoogleRepository {
        val client = Places.createClient(context)
        return GoogleRepositoryImpl(
            dispatcherProvider, googleDataSource, routeDataSource, placeDao, client
        )
    }

    @Provides
    internal fun provideAuthRepository(
        dispatcherProvider: DispatcherProvider,
        userDao: UserDao,
        tokenDao: TokenDao,
        shareTripDataSource: ShareTripDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(
            dispatcherProvider, userDao, tokenDao, shareTripDataSource
        )
    }

    @Provides
    internal fun provideRouteRepository(
        dispatcherProvider: DispatcherProvider,
        shareTripDataSource: ShareTripDataSource,
        tokenDao: TokenDao
    ): RouteRepository = RouteRepositoryImpl(
        dispatcherProvider = dispatcherProvider,
        tokenDao = tokenDao,
        shareTripDataSource = shareTripDataSource
    )
}