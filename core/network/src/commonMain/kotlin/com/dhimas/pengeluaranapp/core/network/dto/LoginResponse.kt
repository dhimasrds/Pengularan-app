package com.dhimas.pengeluaranapp.core.network.dto

import kotlinx.serialization.Serializable
import com.dhimas.pengeluaranapp.core.model.User

@Serializable
data class LoginResponse(
    val token: String,
    val user: User
)

