package com.dhimas.pengeluaranapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.dhimas.pengeluaranapp.di.appModules
import org.koin.core.context.startKoin
import com.dhimas.pengeluaranapp.presentation.screens.LoginScreen

@Composable
fun App() {
    LaunchedEffect(Unit) {
        startKoin {
            modules(appModules)
        }
    }

    MaterialTheme {
        Navigator(LoginScreen()) { navigator ->
            SlideTransition(navigator)
        }
    }
}