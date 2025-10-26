package com.dhimas.pengeluaranapp.di

import com.dhimas.pengeluaranapp.core.data.local.datasource.InMemoryLocalUserDataSource
import com.dhimas.pengeluaranapp.core.data.local.datasource.LocalUserDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDatabaseModule: Module = module {
    single<LocalUserDataSource> { InMemoryLocalUserDataSource() }
}
