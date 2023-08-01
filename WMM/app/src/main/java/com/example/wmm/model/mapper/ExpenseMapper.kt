package com.example.wmm.model.mapper

import com.example.wmm.data.entities.ExpenseData
import com.example.wmm.model.entities.ExpenseEntity
import com.example.wmm.model.entities.TransactionType
import com.example.wmm.utils.DateUtil
import java.util.*

class ExpenseMapper {
    companion object {
        fun convert(expense: ExpenseEntity): ExpenseData = with(expense) {
            ExpenseData(
                name = name,
                amount = amount,
                date = DateUtil.parseDateToStringShortDate(date),
                type = type.name,
                description = description,
                category = category
            )
        }

        fun convert(expense: ExpenseData): ExpenseEntity = with(expense) {
            ExpenseEntity(
                id = id,
                name = name,
                amount = amount,
                date = DateUtil.parseStringShortDateToDate(date),
                type = TransactionType.getTransactionType(type)!!,
                description = description,
                category = category
            )
        }

        fun convertDataToEntityList(expenseList: List<ExpenseData>): List<ExpenseEntity> =
            expenseList.map { convert(it) }

        fun convertEntityToDataList(expenseList: List<ExpenseEntity>): List<ExpenseData> =
            expenseList.map { convert(it) }
    }
}