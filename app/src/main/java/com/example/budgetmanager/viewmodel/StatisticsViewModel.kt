package com.example.budgetmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.budgetmanager.database.AppDatabase
import com.example.budgetmanager.repository.ExpenseRepository

class StatisticsViewModel(application: Application): AndroidViewModel(application) {
    val expenseRepository: ExpenseRepository
    lateinit var expensesSum: LiveData<Map<String, Float>>

    init {
        expenseRepository = ExpenseRepository(AppDatabase.getDatabase(application).expenseDao())
    }

    fun getExpensesSumByType(budgetId: Long) {
        expensesSum = expenseRepository.getExpensesSumByType(budgetId)
    }
}