package com.example.livetracking.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.livetracking.data.local.DataConverter
import com.example.livetracking.domain.entity.PlaceEntity
import com.example.livetracking.domain.entity.TokenEntity
import com.example.livetracking.domain.entity.UserEntity


@Database(
    entities = [
        PlaceEntity::class,
        UserEntity::class,
        TokenEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DataConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
    abstract fun userDao(): UserDao

    abstract fun tokenDao(): TokenDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}