package com.dhimas.pengeluaranapp.di

import org.koin.dsl.module
import com.dhimas.pengeluaranapp.presentation.viewmodel.MainViewModel

val viewModelModule = module {
    factory { MainViewModel() }
}
