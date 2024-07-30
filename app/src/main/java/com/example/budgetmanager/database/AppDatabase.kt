package com.example.budgetmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.budgetmanager.database.dao.BudgetDao
import com.example.budgetmanager.database.dao.ExpenseDao
import com.example.budgetmanager.database.model.BudgetData
import com.example.budgetmanager.database.model.ExpenseData

@Database(entities = [BudgetData::class, ExpenseData::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    abstract fun expenseDao(): ExpenseDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "BudgetTool"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}