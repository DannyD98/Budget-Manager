package com.example.budgetmanager.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class BudgetData(
    @PrimaryKey(autoGenerate = true)
    val budgetId: Long = 0,
    val budgetName: String,
    val budgetValue: Float
)
