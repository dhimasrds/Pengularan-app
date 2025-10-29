package com.dhimas.pengeluaranapp.features.home.impl

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.dhimas.pengeluaranapp.core.domain.repository.UserRepository
import com.dhimas.pengeluaranapp.core.domain.usecase.HomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenModel(private val homeUseCase: HomeUseCase): ScreenModel {
    private val _state = MutableStateFlow(HomeScreenState())
    internal val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    init {
        loadCurrentUser() // ← Load saat screen dibuka
    }

    private fun loadCurrentUser() {
        screenModelScope.launch {
            val user = homeUseCase.getCurrentUser() // ← Panggil getCurrentUser
            _state.update {
                it.copy(
                    totalThisMonth = "$97.50",
                    expenses = listOf(
                        Expense("Groceries", "$50.00", "Food"),
                        Expense("Gas", "$30.00", "Transportation"),
                        Expense("Coffee", "$5.50", "Food"),
                        Expense("Movie Ticket", "$12.00", "Entertainment")
                    ),
                    title = "Hi, ${user?.username ?: user?.email ?: "User"}",
                    userEmail = user?.email ?: ""
                )
            }
        }
    }
}