package com.dhimas.pengeluaranapp.core.network.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.dhimas.pengeluaranapp.core.model.User as DomainUser

@Serializable
data class LoginResponse(
    @SerialName("data")
    val `data`: Data? = null
)

@Serializable
data class Data(
    @SerialName("accessToken")
    val accessToken: String? = null,
    @SerialName("user")
    val user: DomainUser? = null
)

