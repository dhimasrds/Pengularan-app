package com.dhimas.pengeluaranapp.core.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.UnknownHostException

/**
 * Network error interceptor untuk menangani dan logging network errors
 */
class NetworkErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        Log.d(TAG, "=== Network Request ===")
        Log.d(TAG, "URL: $url")
        Log.d(TAG, "Method: ${request.method}")
        Log.d(TAG, "Headers: ${request.headers}")

        return try {
            val response = chain.proceed(request)

            Log.d(TAG, "=== Network Response ===")
            Log.d(TAG, "URL: $url")
            Log.d(TAG, "Status: ${response.code} ${response.message}")
            Log.d(TAG, "Protocol: ${response.protocol}")

            response
        } catch (e: UnknownHostException) {
            Log.e(TAG, "=== DNS Resolution Failed ===")
            Log.e(TAG, "URL: $url")
            Log.e(TAG, "Host: ${request.url.host}")
            Log.e(TAG, "Error: ${e.message}")
            Log.e(TAG, "")
            Log.e(TAG, "Possible Causes:")
            Log.e(TAG, "1. No internet connection")
            Log.e(TAG, "2. DNS server not responding")
            Log.e(TAG, "3. Hostname doesn't exist")
            Log.e(TAG, "4. Firewall/VPN blocking DNS")
            Log.e(TAG, "")
            Log.e(TAG, "Troubleshooting:")
            Log.e(TAG, "1. Check internet connection")
            Log.e(TAG, "2. Try: ping ${request.url.host}")
            Log.e(TAG, "3. Check Private DNS settings")
            Log.e(TAG, "4. Disable VPN if active")

            throw IOException("DNS Resolution Failed: Unable to resolve host '${request.url.host}'. Please check your internet connection.", e)
        } catch (e: IOException) {
            Log.e(TAG, "=== Network Error ===")
            Log.e(TAG, "URL: $url")
            Log.e(TAG, "Error: ${e.javaClass.simpleName}")
            Log.e(TAG, "Message: ${e.message}")

            throw e
        } catch (e: Exception) {
            Log.e(TAG, "=== Unexpected Error ===")
            Log.e(TAG, "URL: $url")
            Log.e(TAG, "Error: ${e.javaClass.simpleName}")
            Log.e(TAG, "Message: ${e.message}", e)

            throw IOException("Network request failed: ${e.message}", e)
        }
    }

    companion object {
        private const val TAG = "NetworkError"
    }
}
