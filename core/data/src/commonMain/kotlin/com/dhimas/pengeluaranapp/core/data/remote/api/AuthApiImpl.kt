package com.dhimas.pengeluaranapp.core.data.remote.api


import com.dhimas.pengeluaranapp.core.network.dto.LoginRequest
import com.dhimas.pengeluaranapp.core.network.dto.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApiImpl(
    private val client: HttpClient,
) : AuthApi {

    override suspend fun login(request: LoginRequest): LoginResponse {
        return client.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}

