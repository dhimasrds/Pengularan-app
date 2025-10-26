package com.dhimas.pengeluaranapp.di

import org.koin.dsl.module
import com.dhimas.pengeluaranapp.presentation.viewmodel.MainViewModel
import com.dhimas.pengeluaranapp.presentation.viewmodel.LoginScreenModel

val viewModelModule = module {
    factory { MainViewModel() }
    factory { LoginScreenModel(get()) }
}
