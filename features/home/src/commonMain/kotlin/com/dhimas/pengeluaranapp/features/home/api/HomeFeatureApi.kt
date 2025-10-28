package com.dhimas.pengeluaranapp.features.home.api

import cafe.adriel.voyager.core.screen.Screen

interface HomeFeatureApi {
    fun entryScreen(onLogout: (() -> Unit)?): Screen
}
