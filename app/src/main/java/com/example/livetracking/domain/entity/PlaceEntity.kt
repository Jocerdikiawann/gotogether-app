package com.example.livetracking.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "tb_place", indices = [Index(value = ["placeId"], unique = true)])
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var placeId: String,
    var namePlace: String,
    var address: String,
    var distance: String,
    var createdAt: Long,
)