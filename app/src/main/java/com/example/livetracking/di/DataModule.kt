package com.example.livetracking.di

import com.example.livetracking.BuildConfig
import com.example.livetracking.data.remote.AppData
import com.example.livetracking.data.remote.design.GoogleDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    internal fun providesGoogleDataSource() : GoogleDataSource = AppData.googleDataSource(
        BuildConfig.BASE_URL_GOOGLE,BuildConfig.MAPS_API_KEYS
    )
}