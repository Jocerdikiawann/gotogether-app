package com.example.livetracking.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_place")
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var namePlace: String,
    var lat: Double,
    var lng: Double,
    var address: String,
    var createdAt: Long,
)