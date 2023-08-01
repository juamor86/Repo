package com.example.wmm.model.entities

enum class TransactionType {
    EXPENSE, INCOME;

    companion object {
        fun getTransactionType(type: String) = values().find { it.name == type }
    }
}
