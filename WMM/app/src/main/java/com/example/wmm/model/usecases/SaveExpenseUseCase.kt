package com.example.wmm.model.usecases

import com.example.wmm.data.persistence.ExpenseDao
import com.example.wmm.model.entities.ExpenseEntity
import com.example.wmm.model.mapper.ExpenseMapper
import javax.inject.Inject

class SaveExpenseUseCase @Inject constructor(private val database: ExpenseDao) {

    private lateinit var expenseEntity: ExpenseEntity

    suspend operator fun invoke() =
        database.insertExpense(ExpenseMapper.convert(expenseEntity))

    fun params(expense: ExpenseEntity) = this.apply {
        this.expenseEntity = expense
    }
}