package com.example.wmm.model.entities

data class TransactionResume(
    var expenseAmount: Float = 0f,
    var incomeAmount: Float = 0f,
    val month: MonthType? = null,
    val dayOfWeek: DayType? = null,
    val weekOfMonth: WeekType? = null
)
