package com.dhimas.pengeluaranapp.core.navigation

import org.koin.dsl.module

 val navigationModule = module {
    single { NavigationCoordinator(get(), get() )}

}