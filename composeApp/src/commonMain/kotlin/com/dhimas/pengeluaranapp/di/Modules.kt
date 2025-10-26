package com.dhimas.pengeluaranapp.di

import com.dhimas.pengeluaranapp.features.login.impl.loginFeatureModule

val appModules = listOf(
    networkModule,
    platformDatabaseModule,
    apiModule,
    repositoryModule,
    viewModelModule,
    loginFeatureModule
)

