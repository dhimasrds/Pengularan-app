package com.dhimas.pengeluaranapp.core.domain.usecase

import com.dhimas.pengeluaranapp.core.domain.repository.UserRepository
import com.dhimas.pengeluaranapp.core.model.User

class GetCurrentUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): User? {
        return userRepository.getCurrentUser()
    }
}