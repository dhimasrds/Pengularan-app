package com.dhimas.pengeluaranapp.di

import org.koin.dsl.module
import com.dhimas.pengeluaranapp.data.remote.api.AuthApi
import com.dhimas.pengeluaranapp.data.remote.api.AuthApiImpl
import com.dhimas.pengeluaranapp.config.AppConfig

val apiModule = module {
    single<AuthApi> {
        AuthApiImpl(get(), AppConfig.apiBaseUrl)
    }
}
