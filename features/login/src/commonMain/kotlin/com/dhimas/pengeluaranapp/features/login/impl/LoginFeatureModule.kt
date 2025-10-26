package com.dhimas.pengeluaranapp.features.login.impl

import com.dhimas.pengeluaranapp.features.login.api.LoginFeatureApi
import org.koin.dsl.module

val loginFeatureModule = module {
    single<LoginFeatureApi> { LoginFeatureApiImpl() }
}

private class LoginFeatureApiImpl : LoginFeatureApi {
    override fun entryScreen() = LoginScreen()
}
