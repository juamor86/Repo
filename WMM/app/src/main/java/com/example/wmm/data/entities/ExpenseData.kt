package com.example.wmm.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense")
data class ExpenseData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val amount: Float,
    val date: String,
    val type: String,
    val category: String,
    val description: String? = null
)
