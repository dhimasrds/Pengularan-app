package com.dhimas.pengeluaranapp.di

import com.dhimas.pengeluaranapp.core.data.coreDataModule
import com.dhimas.pengeluaranapp.core.domain.coreDomainModule
import com.dhimas.pengeluaranapp.core.navigation.navigationModule
import com.dhimas.pengeluaranapp.core.network.coreNetworkModule
import com.dhimas.pengeluaranapp.features.home.impl.homeFeatureModule
import com.dhimas.pengeluaranapp.features.login.impl.loginFeatureModule

val appModules = listOf(
    coreNetworkModule,
    coreDataModule,
    networkModule,
    platformDatabaseModule,
    apiModule,
    repositoryModule,
    viewModelModule,
    loginFeatureModule,
    homeFeatureModule,
    navigationModule,
    coreDomainModule,
)

