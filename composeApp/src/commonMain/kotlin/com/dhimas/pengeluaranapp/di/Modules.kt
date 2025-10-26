package com.dhimas.pengeluaranapp.di

import com.dhimas.pengeluaranapp.core.data.coreDataModule
import com.dhimas.pengeluaranapp.core.network.coreNetworkModule
import com.dhimas.pengeluaranapp.features.login.impl.loginFeatureModule

val appModules = listOf(
    coreNetworkModule,
    coreDataModule,
    networkModule,
    platformDatabaseModule,
    apiModule,
    repositoryModule,
    viewModelModule,
    loginFeatureModule
)

