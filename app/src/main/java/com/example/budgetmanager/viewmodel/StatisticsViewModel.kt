package com.example.budgetmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.budgetmanager.database.AppDatabase
import com.example.budgetmanager.repository.ExpenseRepository

class StatisticsViewModel(application: Application): AndroidViewModel(application) {
    private val expenseRepository: ExpenseRepository
    lateinit var expensesSum: LiveData<Map<String, Float>>
    lateinit var totalExpenses: LiveData<Float>

    init {
        expenseRepository = ExpenseRepository(AppDatabase.getDatabase(application).expenseDao())
    }

    fun getExpenseSumsByBudget(budgetId: Long) {
        expensesSum = expenseRepository.getExpenseSumsByBudget(budgetId)
        totalExpenses = expenseRepository.getAllExpensesSumByBudget(budgetId)
    }
}