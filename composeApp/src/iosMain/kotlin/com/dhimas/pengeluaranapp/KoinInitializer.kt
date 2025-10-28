package com.dhimas.pengeluaranapp

import com.dhimas.pengeluaranapp.di.appModules
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModules)
    }
}

