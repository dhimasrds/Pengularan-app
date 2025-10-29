package com.dhimas.pengeluaranapp.core.domain.usecase

class HomeUseCase(private val getCurrentUserUseCase: GetCurrentUserUseCase) {

    suspend fun getCurrentUser() = getCurrentUserUseCase()
}