package com.dhimas.pengeluaranapp.core.data.remote.client

import com.dhimas.pengeluaranapp.core.network.interceptor.NetworkErrorInterceptor
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
//import okhttp3.Dns
//import java.net.InetAddress
import java.util.concurrent.TimeUnit

actual fun httpClient(): HttpClient = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json {
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 30000
        connectTimeoutMillis = 30000
        socketTimeoutMillis = 30000
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    engine {
        config {
            // Retry on connection failure
            retryOnConnectionFailure(true)

            // Connection timeouts
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)


            // Connection pool
            connectionPool(
                okhttp3.ConnectionPool(
                    maxIdleConnections = 5,
                    keepAliveDuration = 5,
                    timeUnit = TimeUnit.MINUTES
                )
            )

            // Add custom interceptors for better error handling
            addInterceptor { chain ->
                val request = chain.request()
                try {
                    NetworkErrorInterceptor().intercept(chain)
                } catch (e: java.net.UnknownHostException) {
                    android.util.Log.e("HTTP", "UnknownHostException: ${e.message}")
                    android.util.Log.e("HTTP", "Failed to resolve: ${request.url.host}")
                    throw e
                }
            }
        }
    }
}

