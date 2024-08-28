package com.example.budgetmanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
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

    @Query("Select expenseType, sum(expenseVal) as expenseSum from expense where budgetID = :budgetId group by expenseType order by expenseSum DESC")
    fun getExpenseSumsByBudget(budgetId: Long): LiveData<Map<@MapColumn(columnName = "expenseType") String, @MapColumn(columnName = "expenseSum") Float>>

    @Query("Select sum(expenseVal) as expenseSum from expense where budgetID = :budgetId")
    fun getAllExpensesSum(budgetId: Long): LiveData<Float>
}