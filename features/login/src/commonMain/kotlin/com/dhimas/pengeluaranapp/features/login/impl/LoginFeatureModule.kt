package com.dhimas.pengeluaranapp.features.login.impl

import LoginScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.dhimas.pengeluaranapp.features.login.api.LoginFeatureApi
import com.dhimas.pengeluaranapp.core.domain.usecase.LoginUseCase
import org.koin.dsl.module

val loginFeatureModule = module {
    single<LoginFeatureApi> { LoginFeatureApiImpl() }
    factory { LoginScreenModel(get()) }
    factory { LoginUseCase(get()) }
}

private class LoginFeatureApiImpl : LoginFeatureApi {
    override fun entryScreen(onLoginSuccess: (() -> Unit)?): Screen {
        TODO("Not yet implemented")
    }
}
