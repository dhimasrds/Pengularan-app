package com.dhimas.pengeluaranapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.dhimas.pengeluaranapp.di.appModules
import com.dhimas.pengeluaranapp.presentation.screens.LoginScreen
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModules)
    }) {
        MaterialTheme {
            Navigator(LoginScreen()) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}