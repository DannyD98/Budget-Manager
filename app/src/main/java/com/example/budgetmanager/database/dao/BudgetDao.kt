package com.example.budgetmanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetmanager.database.model.BudgetData

@Dao
interface BudgetDao {
    @Query("Select * from budgets")
    fun getBudgetData(): LiveData<List<BudgetData>>

    @Query("Select balanceValue from budgets where budgetId = :budgetID")
    fun getBalance(budgetID: Long): LiveData<Float>

    @Insert
    suspend fun addBudget(budget: BudgetData): Long

    @Delete
    suspend fun removeBudget(budget: BudgetData)

    @Query("Update budgets SET balanceValue = :balance where budgetId = :budgetID")
    suspend fun updateBalance(balance: Float, budgetID: Long)
}