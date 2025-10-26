package com.dhimas.pengeluaranapp.data.local.datasource

import com.dhimas.pengeluaranapp.core.model.User
import kotlinx.coroutines.flow.Flow

interface LocalUserDataSource {
    suspend fun saveUser(user: User?)
    suspend fun getCurrentUser(): User?
    fun observeCurrentUser(): Flow<User?>
}