package com.dhimas.pengeluaranapp.presentation.screens

import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dhimas.pengeluaranapp.presentation.viewmodel.MainViewModel
import com.dhimas.pengeluaranapp.presentation.viewmodel.MainUiState

class MainScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = remember { MainViewModel() }
        val uiState by viewModel.uiState.collectAsState()

        MainScreenContent(
            uiState = uiState
        )
    }
}

@Composable
private fun MainScreenContent(
    uiState: MainUiState
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.error != null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Error: ${uiState.error}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Pengeluaran App",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = uiState.data,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Card(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "KMP Template Features:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text("✓ Kotlin Multiplatform")
                            Text("✓ Compose Multiplatform")
                            Text("✓ Clean Architecture")
                            Text("✓ MVVM Pattern")
                            Text("✓ Koin DI")
                            Text("✓ Voyager Navigation")
                            Text("✓ Ktor Networking")
                            Text("✓ Room Database")
                            Text("✓ BuildKonfig Configuration")
                        }
                    }
                }
            }
        }
    }
}


