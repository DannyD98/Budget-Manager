package com.example.budgetmanager.repository

import androidx.lifecycle.LiveData
import com.example.budgetmanager.database.dao.BudgetDao
import com.example.budgetmanager.database.model.BudgetData

class BudgetRepository(private val budgetDao: BudgetDao) {
    fun getBudgets(): LiveData<List<BudgetData>> = budgetDao.getBudgetData()

    suspend fun addBudget(budgetData: BudgetData) {
        budgetDao.addBudget(budgetData)
    }

    suspend fun deleteBudget(budgetData: BudgetData) {
        budgetDao.deleteBudget(budgetData)
    }
}