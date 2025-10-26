package com.dhimas.pengeluaranapp.core.domain

import com.dhimas.pengeluaranapp.core.domain.usecase.LoginUseCase
import org.koin.dsl.module

val coreDomainModule = module {
    factory { LoginUseCase(userRepository = get()) }
}

