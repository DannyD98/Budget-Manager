package com.example.budgetmanager.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetData(
    @PrimaryKey(autoGenerate = true)
    var budgetId: Long = 0,
    val budgetName: String,
    val budgetValue: Float,
    val balanceValue: Float
)
