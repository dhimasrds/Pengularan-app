package com.dhimas.pengeluaranapp.di

import org.koin.dsl.module
import com.dhimas.pengeluaranapp.core.data.remote.client.httpClient

val networkModule = module {
    single { httpClient() }
}
