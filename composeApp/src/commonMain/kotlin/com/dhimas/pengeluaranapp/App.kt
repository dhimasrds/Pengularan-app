package com.dhimas.pengeluaranapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.dhimas.pengeluaranapp.di.appModules
import com.dhimas.pengeluaranapp.features.login.api.LoginFeatureApi
import com.dhimas.pengeluaranapp.presentation.screens.MainScreen
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModules)
    }) {
        MaterialTheme {
            val loginFeatureApi = koinInject<LoginFeatureApi>()
            Navigator(
                screen = loginFeatureApi.entryScreen()
            ) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}