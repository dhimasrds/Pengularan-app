package com.dhimas.pengeluaranapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dhimas.pengeluaranapp.core.navigation.NavigationCoordinator
import org.koin.compose.koinInject

/**
 * Initial screen that sets up navigation and displays the appropriate start screen
 */
class InitialScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val coordinator: NavigationCoordinator = koinInject()

        // Set up initial navigation
        LaunchedEffect(Unit) {
            // Replace this screen with the actual login screen with proper callbacks
            coordinator.navigateToLogin(navigator)
        }
    }
}
