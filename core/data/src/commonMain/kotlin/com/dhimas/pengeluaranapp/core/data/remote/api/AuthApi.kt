package com.dhimas.pengeluaranapp.core.data.remote.api

import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Body
import com.dhimas.pengeluaranapp.core.network.dto.LoginRequest
import com.dhimas.pengeluaranapp.core.network.dto.LoginResponse
import de.jensklingenberg.ktorfit.http.GET

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

