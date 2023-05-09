package com.example.livetracking.di

import android.content.Context
import com.example.livetracking.BuildConfig
import com.example.livetracking.data.coroutines.DefaultDispatcherProvider
import com.example.livetracking.data.coroutines.DispatcherProvider
import com.example.livetracking.data.local.room.AppDatabase
import com.example.livetracking.data.local.room.PlaceDao
import com.example.livetracking.data.remote.AppData
import com.example.livetracking.data.remote.design.GoogleDataSource
import com.example.livetracking.data.remote.design.RoutesDataSource
import com.example.livetracking.repository.design.GoogleRepository
import com.example.livetracking.repository.impl.GoogleRepositoryImpl
import com.google.android.libraries.places.api.Places
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
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
}