package com.dhimas.pengeluaranapp.data.remote.api

import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Body
import com.dhimas.pengeluaranapp.core.network.dto.LoginRequest
import com.dhimas.pengeluaranapp.core.network.dto.LoginResponse

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
