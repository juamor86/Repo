package com.example.wmm.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wmm.data.entities.ExpenseData

@Database(entities = [ExpenseData::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getExpenseDao():ExpenseDao
}