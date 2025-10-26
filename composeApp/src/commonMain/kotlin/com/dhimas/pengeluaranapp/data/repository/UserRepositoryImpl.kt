package com.dhimas.pengeluaranapp.data.repository

import kotlinx.coroutines.flow.Flow
import com.dhimas.pengeluaranapp.domain.repository.UserRepository
import com.dhimas.pengeluaranapp.domain.model.User
import com.dhimas.pengeluaranapp.data.remote.api.AuthApi
import com.dhimas.pengeluaranapp.data.remote.dto.LoginRequest
import com.dhimas.pengeluaranapp.data.local.datasource.LocalUserDataSource

class UserRepositoryImpl(
    private val localUserDataSource: LocalUserDataSource,
    private val authApi: AuthApi
) : UserRepository {

    override suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            val user = User(
                id = response.user.id,
                username = response.user.username,
                email = response.user.email,
                createdAt = response.user.createdAt,
                updatedAt = response.user.updatedAt
            )
            saveUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveUser(user: User): Result<Unit> {
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
