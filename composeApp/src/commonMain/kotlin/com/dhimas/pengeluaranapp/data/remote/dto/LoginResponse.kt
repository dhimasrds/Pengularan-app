package com.dhimas.pengeluaranapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val email: String,
    val createdAt: Long,
    val updatedAt: Long
)
