package com.dhimas.pengeluaranapp.core.data

import com.dhimas.pengeluaranapp.core.data.local.datasource.LocalUserDataSource
import com.dhimas.pengeluaranapp.core.data.local.datasource.LocalUserDataSourceImpl
import com.dhimas.pengeluaranapp.core.data.remote.api.AuthApi
import com.dhimas.pengeluaranapp.core.data.remote.api.AuthApiImpl
import com.dhimas.pengeluaranapp.core.data.repository.UserRepositoryImpl
import com.dhimas.pengeluaranapp.core.domain.repository.UserRepository
import org.koin.dsl.module

val coreDataModule = module {
    // Local data source
    single<LocalUserDataSource> { LocalUserDataSourceImpl() }
    
    // Remote API
    single<AuthApi> {
        AuthApiImpl(client = get())
    }
    
    // Repository
    single<UserRepository> { 
        UserRepositoryImpl(
            localUserDataSource = get(),
            authApi = get()
        ) 
    }
}
