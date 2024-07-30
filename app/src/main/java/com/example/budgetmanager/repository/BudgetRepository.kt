package com.example.budgetmanager.repository

import androidx.lifecycle.LiveData
import com.example.budgetmanager.database.dao.BudgetDao
import com.example.budgetmanager.database.model.BudgetData

class BudgetRepository(private val budgetDao: BudgetDao) {
    fun getBudgets(): LiveData<List<BudgetData>> = budgetDao.getBudgetData()

    suspend fun addBudget(budgetData: BudgetData): Long {
        return budgetDao.addBudget(budgetData)
    }

    suspend fun removeBudget(budgetData: BudgetData) {
        budgetDao.removeBudget(budgetData)
    }

    fun getBalance(budgetId: Long): LiveData<Float> {
        return budgetDao.getBalance(budgetId)
    }

    suspend fun updateBalance(balance: Float, budgetID: Long) {
        budgetDao.updateBalance(balance, budgetID)
    }
}