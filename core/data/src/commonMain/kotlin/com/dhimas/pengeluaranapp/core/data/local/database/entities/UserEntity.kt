package com.dhimas.pengeluaranapp.core.data.local.database.entities

data class UserEntity(
    var id: String = "",
    var username: String = "",
    var email: String = "",
    var createdAt: Long = 0,
    var updatedAt: Long = 0
)

