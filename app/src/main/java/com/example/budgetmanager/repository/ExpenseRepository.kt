package com.example.budgetmanager.repository

import androidx.lifecycle.LiveData
import com.example.budgetmanager.database.dao.ExpenseDao
import com.example.budgetmanager.database.model.ExpenseData

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    fun getExpensesByBudget(budgetId: Long): LiveData<List<ExpenseData>> = expenseDao.getExpenses(budgetId)

    suspend fun addExpense(expenseData: ExpenseData) {
        expenseDao.addExpense(expenseData)
    }

    suspend fun deleteExpense(expenseData: ExpenseData) {
        expenseDao.deleteExpense(expenseData)
    }

    suspend fun updateExpense(expenseData: ExpenseData) {
        expenseDao.updateExpense(expenseData)
    }

    fun getExpenseSumsByBudget(budgetId: Long): LiveData<Map<String, Float>> = expenseDao.getExpenseSumsByBudget(budgetId)

    fun getAllExpensesSumByBudget(budgetId: Long): LiveData<Float> {
        return expenseDao.getAllExpensesSumByBudget(budgetId)
    }
}