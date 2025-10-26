package com.dhimas.pengeluaranapp.core.domain.usecase

import com.dhimas.pengeluaranapp.core.domain.repository.UserRepository
import com.dhimas.pengeluaranapp.core.model.User

class LoginUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User?> {
        return try {
            if (email.isBlank() || password.isBlank()) {
                Result.failure(Exception("Email and password cannot be empty"))
            } else {
                userRepository.loginUser(email, password)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

