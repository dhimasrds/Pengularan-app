package com.dhimas.pengeluaranapp.core.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String? = null,
    val email: String,
    val accessToken: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

