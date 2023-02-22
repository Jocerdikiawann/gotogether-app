package com.example.livetracking.di

import android.app.Activity
import android.content.Context
import com.example.livetracking.BuildConfig
import com.example.livetracking.data.coroutines.DefaultDispatcherProvider
import com.example.livetracking.data.coroutines.DispatcherProvider
import com.example.livetracking.data.local.room.AppDatabase
import com.example.livetracking.data.local.room.PlaceDao
import com.example.livetracking.data.remote.AppData
import com.example.livetracking.data.remote.design.GoogleDataSource
import com.example.livetracking.repository.design.GoogleRepository
import com.example.livetracking.repository.impl.GoogleRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
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
        googleDataSource: GoogleDataSource
    ): GoogleRepository = GoogleRepositoryImpl(
        dispatcherProvider, googleDataSource
    )
}