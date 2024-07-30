package com.example.budgetmanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.budgetmanager.database.model.ExpenseData

@Dao
interface ExpenseDao {
    @Query("Select * from expense where budgetID = :budgetId")
    fun getExpenses(budgetId: Long): LiveData<List<ExpenseData>>

    @Insert
    suspend fun addExpense(expenseData: ExpenseData)

    @Delete
    suspend fun deleteExpense(expenseData: ExpenseData)

    @Update
    suspend fun updateExpense(expenseData: ExpenseData)
}