package com.dhimas.pengeluaranapp.core.data.local.datasource

import com.dhimas.pengeluaranapp.core.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalUserDataSourceImpl : LocalUserDataSource {
    private val _currentUser = MutableStateFlow<User?>(null)

    override suspend fun saveUser(user: User?) {
        _currentUser.value = user
    }

    override suspend fun getCurrentUser(): User? {
        return _currentUser.value
    }

    override fun observeCurrentUser(): Flow<User?> {
        return _currentUser.asStateFlow()
    }
}

