package com.dhimas.pengeluaranapp.core.data.repository

import com.dhimas.pengeluaranapp.core.data.local.datasource.LocalUserDataSource
import com.dhimas.pengeluaranapp.core.data.remote.api.AuthApi
import com.dhimas.pengeluaranapp.core.domain.repository.UserRepository
import com.dhimas.pengeluaranapp.core.model.User
import com.dhimas.pengeluaranapp.core.network.dto.LoginRequest
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val localUserDataSource: LocalUserDataSource,
    private val authApi: AuthApi
) : UserRepository {

    override suspend fun loginUser(email: String, password: String): Result<User?> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            val user = response.data?.user
            saveUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveUser(user: User?): Result<Unit> {
        return try {
            localUserDataSource.saveUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        return localUserDataSource.getCurrentUser()
    }

    override fun observeCurrentUser(): Flow<User?> {
        return localUserDataSource.observeCurrentUser()
    }
}

