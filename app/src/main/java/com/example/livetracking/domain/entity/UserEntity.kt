package com.example.livetracking.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 1,
    var email: String,
    var fullName: String,
    var googleId: String,
)

@Entity(tableName = "tb_token")
data class TokenEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 1,
    var token: String,
)