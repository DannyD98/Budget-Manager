package com.example.budgetmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.budgetmanager.database.AppDatabase
import com.example.budgetmanager.database.model.ExpenseData
import com.example.budgetmanager.repository.BudgetRepository
import com.example.budgetmanager.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application): AndroidViewModel(application) {
    private val expenseRepository: ExpenseRepository
    private val budgetRepository: BudgetRepository
    lateinit var expenses : LiveData<List<ExpenseData>>
    lateinit var balance: LiveData<Float>

    init {
        expenseRepository = ExpenseRepository(AppDatabase.getDatabase(application).expenseDao())
        budgetRepository = BudgetRepository(AppDatabase.getDatabase(application).budgetDao())
    }

    fun loadExpenses(budgetId: Long) {
        expenses = expenseRepository.getExpenses(budgetId)
        balance = budgetRepository.getBalance(budgetId)
    }

    fun addExpense(expenseData: ExpenseData) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.addExpense(expenseData)
        }
    }

    fun deleteExpense(expenseData: ExpenseData) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.deleteExpense(expenseData)
        }
    }

    fun updateExpense(expenseData: ExpenseData) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.updateExpense(expenseData)
        }
    }

    fun updateBalance(balance: Float, budgetId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            budgetRepository.updateBalance(balance, budgetId)
        }
    }
}