package com.example.wmm.model.entities

import java.util.*

data class ExpenseEntity(
    val id: Long,
    val name: String,
    val amount: Float,
    val date: Date,
    val type: TransactionType,
    val category: String,
    val description: String? = null
)
