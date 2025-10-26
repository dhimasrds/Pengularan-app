package com.dhimas.pengeluaranapp.core.domain.repository

import kotlinx.coroutines.flow.Flow
import com.dhimas.pengeluaranapp.core.model.User

interface UserRepository {
    suspend fun loginUser(email: String, password: String): Result<User?>
    suspend fun saveUser(user: User?): Result<Unit>
    suspend fun getCurrentUser(): User?
    fun observeCurrentUser(): Flow<User?>
}

