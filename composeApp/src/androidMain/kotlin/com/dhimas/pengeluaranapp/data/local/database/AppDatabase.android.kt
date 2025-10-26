package com.dhimas.pengeluaranapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dhimas.pengeluaranapp.data.local.room.dao.UserDao
import com.dhimas.pengeluaranapp.data.local.room.entity.UserRoomEntity

@Database(
    entities = [UserRoomEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
