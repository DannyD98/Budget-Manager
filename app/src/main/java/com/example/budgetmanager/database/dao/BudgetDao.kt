package com.example.budgetmanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetmanager.database.model.BudgetData

@Dao
interface BudgetDao {
    @Query("Select * from budget")
    fun getBudgetData(): LiveData<List<BudgetData>>

    @Insert
    suspend fun addBudget(budget: BudgetData)

    @Delete
    suspend fun deleteBudget(budget: BudgetData)
}