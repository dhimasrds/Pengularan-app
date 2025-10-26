package com.dhimas.pengeluaranapp.data.local.datasource

import com.dhimas.pengeluaranapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class InMemoryLocalUserDataSource : LocalUserDataSource {
    private val userFlow = MutableStateFlow<User?>(null)

    override suspend fun saveUser(user: User) {
        userFlow.value = user
    }

    override suspend fun getCurrentUser(): User? = userFlow.value

    override fun observeCurrentUser(): Flow<User?> = userFlow
}
