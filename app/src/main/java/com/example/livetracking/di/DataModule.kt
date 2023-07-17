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
import com.example.livetracking.repository.design.AuthRepository
import com.example.livetracking.repository.design.GoogleRepository
import com.example.livetracking.repository.design.RouteRepository
import com.example.livetracking.repository.impl.AuthRepositoryImpl
import com.example.livetracking.repository.impl.GoogleRepositoryImpl
import com.example.livetracking.repository.impl.RouteRepositoryImpl
import com.google.android.libraries.places.api.Places
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.grpc.ManagedChannel

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    internal fun provideChannelGrpc(): ManagedChannel =
        AppData.initChannel("192.168.1.2", 8888)

    @Provides
    internal fun providesGoogleDataSource(): GoogleDataSource = AppData.googleDataSource(
        BuildConfig.BASE_URL_GOOGLE, BuildConfig.MAPS_API_KEYS
    )

    @Provides
    internal fun providesRoutesDataSource(): RoutesDataSource = AppData.routesDataSource(
        BuildConfig.MAPS_API_KEYS, BuildConfig.BASE_URL_ROUTES_API
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
        channel: ManagedChannel,
        userDao: UserDao,
        tokenDao: TokenDao
    ): AuthRepository {
        return AuthRepositoryImpl(
            channel, dispatcherProvider,userDao, tokenDao
        )
    }

    @Provides
    internal fun provideRouteRepository(
        dispatcherProvider: DispatcherProvider,
        channel: ManagedChannel,
        userDao: UserDao,
        tokenDao: TokenDao
    ): RouteRepository = RouteRepositoryImpl(
        dispatcherProvider = dispatcherProvider,
        channel = channel,
        tokenDao = tokenDao,
        userDao =userDao
    )
}