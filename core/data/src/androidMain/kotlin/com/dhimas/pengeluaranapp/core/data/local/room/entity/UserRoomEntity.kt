package com.dhimas.pengeluaranapp.core.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserRoomEntity(
    @PrimaryKey val id: String,
    val username: String,
    val email: String,
    val createdAt: Long,
    val updatedAt: Long
)

