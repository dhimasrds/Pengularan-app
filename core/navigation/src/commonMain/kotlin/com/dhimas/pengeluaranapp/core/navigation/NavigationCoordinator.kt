package com.dhimas.pengeluaranapp.core.navigation

import cafe.adriel.voyager.navigator.Navigator
import com.dhimas.pengeluaranapp.features.home.api.HomeFeatureApi
import com.dhimas.pengeluaranapp.features.login.api.LoginFeatureApi
/**
 * Centralized navigation coordinator that handles all navigation flows
 * between different feature modules in a clean architecture way.
 */
class NavigationCoordinator(
    private val loginFeatureApi: LoginFeatureApi,
    private val homeFeatureApi: HomeFeatureApi
) {


    /**
     * Navigate to login screen with success callback
     */
    fun navigateToLogin(navigator: Navigator) {
        val loginScreen = loginFeatureApi.entryScreen {
            navigateToHome(navigator)
        }
        navigator.replaceAll(loginScreen)
    }

    /**
     * Navigate to home screen and clear login from stack
     */
    fun navigateToHome(navigator: Navigator) {
        val homeScreen = homeFeatureApi.entryScreen {
            // Handle logout - navigate back to login
            navigateToLoginAndClearStack(navigator)
        }
        // Replace current screen with home screen (removes login from stack)
        navigator.replaceAll(homeScreen)
    }

    /**
     * Navigate back to login (for logout)
     */
    fun navigateToLoginAndClearStack(navigator: Navigator) {
        val loginScreen = loginFeatureApi.entryScreen {
            navigateToHome(navigator)
        }
        navigator.replaceAll(loginScreen)
    }

    /**
     * Get initial screen based on authentication state
     */
//    fun getInitialScreen(isAuthenticated: Boolean = false) = if (isAuthenticated) {
//        homeFeatureApi.entryScreen()
//    } else {
//        loginFeatureApi.entryScreen {
//            // This callback will be handled by the navigator in App.kt
//        }
//    }
}
