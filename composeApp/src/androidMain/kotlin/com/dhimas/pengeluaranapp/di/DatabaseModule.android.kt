package com.dhimas.pengeluaranapp.di

import com.dhimas.pengeluaranapp.config.AppConfig
import com.dhimas.pengeluaranapp.core.data.local.database.AppDatabase
import com.dhimas.pengeluaranapp.core.data.local.datasource.AndroidRoomLocalUserDataSource
import com.dhimas.pengeluaranapp.core.data.local.datasource.InMemoryLocalUserDataSource
import com.dhimas.pengeluaranapp.core.data.local.datasource.LocalUserDataSource
import com.dhimas.pengeluaranapp.util.AndroidContextHolder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDatabaseModule: Module = module {
    single<LocalUserDataSource> {
        try {
            val db = androidx.room.Room.databaseBuilder(
                AndroidContextHolder.context,
                AppDatabase::class.java,
                AppConfig.databaseName
            ).build()
            AndroidRoomLocalUserDataSource(db.userDao())
        } catch (t: Throwable) {
            InMemoryLocalUserDataSource()
        }
    }
}
