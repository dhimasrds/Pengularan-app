package com.dhimas.pengeluaranapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform