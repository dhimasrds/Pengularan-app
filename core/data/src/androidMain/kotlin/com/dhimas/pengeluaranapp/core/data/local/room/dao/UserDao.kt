package com.dhimas.pengeluaranapp.core.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dhimas.pengeluaranapp.core.data.local.room.entity.UserRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserRoomEntity)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getCurrent(): UserRoomEntity?

    @Query("SELECT * FROM users LIMIT 1")
    fun observeCurrent(): Flow<UserRoomEntity?>

    @Query("DELETE FROM users")
    suspend fun clearAll()
}

