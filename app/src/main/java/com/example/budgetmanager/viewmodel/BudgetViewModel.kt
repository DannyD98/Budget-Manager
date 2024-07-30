package com.example.budgetmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.budgetmanager.database.AppDatabase
import com.example.budgetmanager.database.model.BudgetData
import com.example.budgetmanager.repository.BudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application): AndroidViewModel(application) {
    private val budgetRepository: BudgetRepository
    var budgets: LiveData<List<BudgetData>>

    init {
        val budgetDao = AppDatabase.getDatabase(application).budgetDao()
        budgetRepository = BudgetRepository(budgetDao)

//        budgets = liveData(Dispatchers.IO) {
//            emit(budgetRepository.getBudgets())
//        }
        budgets = budgetRepository.getBudgets()
    }

    fun addBudget(budgetData: BudgetData) {
        viewModelScope.launch(Dispatchers.IO) {
            budgetRepository.addBudget(budgetData)
        }
    }

    fun removeBudget(budgetData: BudgetData) {
        viewModelScope.launch(Dispatchers.IO) {
            budgetRepository.removeBudget(budgetData)
        }
    }
}