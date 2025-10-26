package com.dhimas.pengeluaranapp.di

import org.koin.dsl.module
import com.dhimas.pengeluaranapp.domain.repository.UserRepository
import com.dhimas.pengeluaranapp.data.repository.UserRepositoryImpl
import com.dhimas.pengeluaranapp.domain.usecase.LoginUseCase

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    factory { LoginUseCase(get()) }
}
