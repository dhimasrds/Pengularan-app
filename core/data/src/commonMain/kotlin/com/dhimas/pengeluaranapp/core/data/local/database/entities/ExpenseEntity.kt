package com.dhimas.pengeluaranapp.core.data.local.database.entities

data class ExpenseEntity(
    var id: String = "",
    var title: String = "",
    var amount: Double = 0.0,
    var description: String = "",
    var category: String = "",
    var userId: String = "",
    var createdAt: Long = 0,
    var updatedAt: Long = 0
)

