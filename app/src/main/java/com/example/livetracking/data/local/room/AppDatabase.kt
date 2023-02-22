package com.example.livetracking.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.livetracking.domain.entity.PlaceEntity


@Database(
    entities = [
        PlaceEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeDao() : PlaceDao

    companion object {
        const val DATABASE_NAME="app_db"
    }
}