package com.example.livetracking.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.livetracking.domain.entity.PlaceEntity


@Dao
interface PlaceDao {
    @Query("SELECT * FROM tb_place ORDER BY createdAt DESC LIMIT 10")
    fun getAllHistory(): List<PlaceEntity>

    @Query("SELECT * FROM tb_place WHERE id = :id")
    fun getById(id: Int): PlaceEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(place: PlaceEntity): Long

    @Query("DELETE FROM tb_place WHERE id = :id")
    fun delete(id: Int)

    @Update
    fun update(place: PlaceEntity)
}