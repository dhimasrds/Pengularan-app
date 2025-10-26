package com.dhimas.pengeluaranapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,
    val createdAt: Long,
    val updatedAt: Long
)
