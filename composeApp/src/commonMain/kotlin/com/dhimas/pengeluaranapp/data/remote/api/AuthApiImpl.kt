package com.dhimas.pengeluaranapp.data.remote.api

import com.dhimas.pengeluaranapp.data.remote.dto.LoginRequest
import com.dhimas.pengeluaranapp.data.remote.dto.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApiImpl(
    private val client: HttpClient,
    private val baseUrl: String
) : AuthApi {

    override suspend fun login(request: LoginRequest): LoginResponse {
        val url = joinUrl(baseUrl, "auth/login")
        return client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    private fun joinUrl(base: String, path: String): String {
        val b = if (base.endsWith("/")) base.dropLast(1) else base
        val p = if (path.startsWith("/")) path.drop(1) else path
        return "$b/$p"
    }
}
