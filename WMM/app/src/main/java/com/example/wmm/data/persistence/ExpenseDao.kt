package com.example.wmm.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wmm.data.entities.ExpenseData

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense")
    suspend fun getAllExpenses(): List<ExpenseData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseData)
}