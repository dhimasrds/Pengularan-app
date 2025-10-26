package com.dhimas.pengeluaranapp.data.local.datasource

import com.dhimas.pengeluaranapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface LocalUserDataSource {
    suspend fun saveUser(user: User)
    suspend fun getCurrentUser(): User?
    fun observeCurrentUser(): Flow<User?>
}