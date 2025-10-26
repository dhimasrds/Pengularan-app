package com.dhimas.pengeluaranapp.features.login.api

import cafe.adriel.voyager.core.screen.Screen

interface LoginFeatureApi {
    fun entryScreen(onLoginSuccess: (() -> Unit)? = null): Screen
}
