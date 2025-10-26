package com.dhimas.pengeluaranapp.di

import org.koin.dsl.module
import com.dhimas.pengeluaranapp.core.domain.repository.UserRepository
import com.dhimas.pengeluaranapp.core.data.repository.UserRepositoryImpl

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
}
