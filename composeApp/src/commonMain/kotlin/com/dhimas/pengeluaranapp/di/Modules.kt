package com.dhimas.pengeluaranapp.di

import com.dhimas.pengeluaranapp.di.networkModule
import com.dhimas.pengeluaranapp.di.viewModelModule
import com.dhimas.pengeluaranapp.di.apiModule
import com.dhimas.pengeluaranapp.di.repositoryModule

val appModules = listOf(
    networkModule,
    platformDatabaseModule,
    apiModule,
    repositoryModule,
    viewModelModule
)

