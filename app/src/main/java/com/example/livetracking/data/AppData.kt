package com.example.livetracking.data

import android.content.Context
import androidx.room.Room
import com.example.livetracking.data.local.Persistence
import com.example.livetracking.data.local.room.AppDatabase
import com.example.livetracking.data.remote.design.GoogleDataSource
import com.example.livetracking.data.remote.design.RoutesDataSource
import com.example.livetracking.data.remote.design.ShareTripDataSource
import com.example.livetracking.data.remote.impl.GoogleDataSourceImpl
import com.example.livetracking.data.remote.impl.RoutesDataSourceImpl
import com.example.livetracking.data.remote.impl.ShareTripDataSourceImpl
import com.example.livetracking.data.remote.services.ClientShareTripApiServices
import com.example.livetracking.data.remote.services.GoogleApiServices
import com.example.livetracking.data.remote.services.RoutesApiServices
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
                    val url = origin.url.newBuilder().addQueryParameter("key", apiKey)
                        .build()
                    val request =
                        origin.newBuilder().url(url).method(origin.method, origin.body).build()
                    chain.proceed(request)
                }
                .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://${baseUrl}/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            val service = retrofit.create(GoogleApiServices::class.java)

            return GoogleDataSourceImpl(service)
        }

        fun routesDataSource(
            apiKey: String,
            baseUrl: String
        ): RoutesDataSource {
            val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor { chain ->
                    val origin = chain.request()
                    val request =
                        origin.newBuilder().method(origin.method, origin.body)
                            .addHeader("X-Goog-Api-Key", apiKey).build()
                    chain.proceed(request)
                }
                .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://${baseUrl}/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            val service = retrofit.create(RoutesApiServices::class.java)
            return RoutesDataSourceImpl(service)
        }


        fun clientShareTripSource(
            baseUrl: String
        ): ShareTripDataSource {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .build()

            val retro = Retrofit.Builder()
                .baseUrl("http://$baseUrl/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            val service = retro.create(ClientShareTripApiServices::class.java)
            return ShareTripDataSourceImpl(service)
        }

        fun initDatabase(appContext: Context): AppDatabase = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

        fun initPersistence(appContext: Context): Persistence =
            Persistence(appContext.getSharedPreferences("g0to2", Context.MODE_PRIVATE))
    }
}