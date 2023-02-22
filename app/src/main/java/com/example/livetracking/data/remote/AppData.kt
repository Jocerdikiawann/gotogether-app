package com.example.livetracking.data.remote

import android.content.Context
import androidx.room.Room
import com.example.livetracking.data.local.room.AppDatabase
import com.example.livetracking.data.remote.design.GoogleDataSource
import com.example.livetracking.data.remote.impl.GoogleDataSourceImpl
import com.example.livetracking.data.remote.services.GoogleApiServices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppData {
    companion object {
        private val REQUEST_TIMEOUT = 5
        private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        fun googleDataSource(
            baseUrl: String,
            apiKey: String,
        ): GoogleDataSource {
            val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor { chain ->
                    val origin = chain.request()
                    val request = origin.newBuilder()
                        .method(origin.method, origin.body)
                        .build()
                    chain.proceed(request)
                }
                .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://${baseUrl}/?key=${apiKey}")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            val service = retrofit.create(GoogleApiServices::class.java)

            return GoogleDataSourceImpl(service)
        }

        fun initDatabase(appContext: Context): AppDatabase = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}