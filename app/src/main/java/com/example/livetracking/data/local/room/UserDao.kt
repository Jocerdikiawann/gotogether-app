package com.example.livetracking.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.livetracking.domain.entity.TokenEntity
import com.example.livetracking.domain.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM tb_user WHERE id = 1")
     fun getUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(userEntity: UserEntity)

    @Query("DELETE FROM tb_user WHERE id = 1")
     fun delete()
}

@Dao
interface TokenDao {
    @Query("SELECT * FROM tb_token WHERE id = 1")
     fun getToken(): TokenEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(token: TokenEntity)

    @Query("DELETE FROM tb_token WHERE id = 1")
     fun delete()
}