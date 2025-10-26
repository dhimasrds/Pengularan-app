package com.dhimas.pengeluaranapp.data.remote.client

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

actual fun httpClient(): HttpClient = HttpClient(Darwin) {
    install(ContentNegotiation) {
        json(Json {
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 15000
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
        // HttpTimeout plugin handles request/resource timeouts
        configureSession {
            // Additional NSURLSession configuration if needed
        }
    }
}
