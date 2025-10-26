package com.dhimas.pengeluaranapp.features.home.impl

import com.dhimas.pengeluaranapp.features.home.api.HomeFeatureApi
import org.koin.dsl.module

val homeFeatureModule = module {
    single<HomeFeatureApi> { HomeFeatureApiImpl() }
}

private class HomeFeatureApiImpl : HomeFeatureApi {
    override fun entryScreen() = HomeScreen()
}
