package com.dhimas.pengeluaranapp.di

import org.koin.dsl.module
import com.dhimas.pengeluaranapp.core.data.remote.api.AuthApi
import com.dhimas.pengeluaranapp.core.data.remote.api.AuthApiImpl

val apiModule = module {
    single<AuthApi> {
        AuthApiImpl(get())
    }
}
