package com.example.wmm.model.usecases

import com.example.wmm.data.persistence.ExpenseDao
import com.example.wmm.model.entities.ExpenseEntity
import com.example.wmm.model.mapper.ExpenseMapper
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(private val database: ExpenseDao) {

    suspend operator fun invoke(): List<ExpenseEntity> {
        return database.getAllExpenses().map { ExpenseMapper.convert(it) }
    }

}