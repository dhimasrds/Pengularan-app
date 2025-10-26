package com.dhimas.pengeluaranapp.core.data.local.datasource

import com.dhimas.pengeluaranapp.core.data.local.room.dao.UserDao
import com.dhimas.pengeluaranapp.core.data.local.room.entity.UserRoomEntity
import com.dhimas.pengeluaranapp.core.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AndroidRoomLocalUserDataSource(
    private val userDao: UserDao
) : LocalUserDataSource {

    override suspend fun saveUser(user: User) {
        userDao.upsert(user.toRoom())
    }

    override suspend fun getCurrentUser(): User? {
        return userDao.getCurrent()?.toDomain()
    }

    override fun observeCurrentUser(): Flow<User?> {
        return userDao.observeCurrent().map { it?.toDomain() }
    }
}

private fun User.toRoom() = UserRoomEntity(
    id = id,
    username = username,
    email = email,
    createdAt = createdAt,
    updatedAt = updatedAt
)

private fun UserRoomEntity.toDomain() = User(
    id = id,
    username = username,
    email = email,
    createdAt = createdAt,
    updatedAt = updatedAt
)

