package com.dhimas.pengeluaranapp.core.domain

import com.dhimas.pengeluaranapp.core.domain.usecase.GetCurrentUserUseCase
import com.dhimas.pengeluaranapp.core.domain.usecase.HomeUseCase
import com.dhimas.pengeluaranapp.core.domain.usecase.LoginUseCase
import org.koin.dsl.module

val coreDomainModule = module {
    // UseCases
    factory { GetCurrentUserUseCase(get()) }
    factory {
        HomeUseCase(
            getCurrentUserUseCase = get()
        )
    }
    factory { LoginUseCase(userRepository = get()) }
}

