package com.dhimas.pengeluaranapp.features.home.impl

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.koin.compose.koinInject

// ============================================================
// Data Models
// ============================================================

internal data class Expense(
    val title: String,
    val amount: String,
    val category: String
)

internal data class HomeScreenState(
    val title: String = "Expense Tracker",
    val userEmail: String = "", // ← Add this
    val totalThisMonth: String = "$0.00",
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = false)

// ============================================================
// Screen (with Navigation & Dependencies)
// ============================================================

class HomeScreen(
    private val onLogout: (() -> Unit)? = null
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinInject<HomeScreenModel>() // ← Inject ScreenModel
        val state by screenModel.state.collectAsState()


        HomeScreenContent(
            state = state,
            onLogoutClick = {
                onLogout?.invoke()
            },
            onAddExpenseClick = {
                // TODO: Navigate to Add Expense screen
            }
        )
    }
}

// ============================================================
// Stateless UI Component (Reusable & Previewable)
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreenContent(
    state: HomeScreenState,
    onLogoutClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        state.title,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddExpenseClick) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Expense"
                )
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    TotalCard(totalAmount = state.totalThisMonth)
                }

                item {
                    Text(
                        "Recent Expenses",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(state.expenses) { expense ->
                    ExpenseItem(expense = expense)
                }
            }
        }
    }
}

// ============================================================
// UI Components
// ============================================================

@Composable
private fun TotalCard(
    totalAmount: String,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = Modifier.height(16.dp))
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Total This Month",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                totalAmount,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun ExpenseItem(
    expense: Expense,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = expense.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = expense.amount,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


