package com.dhimas.pengeluaranapp.features.home.impl

import com.dhimas.pengeluaranapp.features.home.api.HomeFeatureApi
import org.koin.dsl.module

val homeFeatureModule = module {
    single<HomeFeatureApi> { HomeFeatureApiImpl() }
    factory {
        HomeScreenModel(
            homeUseCase = get() // ← Koin inject
        )
    }
}

private class HomeFeatureApiImpl : HomeFeatureApi {
    override fun entryScreen(onLogout: (() -> Unit)?) = HomeScreen(onLogout)
}
