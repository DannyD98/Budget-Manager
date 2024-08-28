package com.example.budgetmanager.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = BudgetData::class,
            parentColumns = ["budgetId"],
            childColumns = ["budgetID"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    tableName = "expense"
)
data class ExpenseData(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Long = 0,
    var expenseVal: Float,
    var expenseDescription: String,
    var expenseDate: String,
    var expenseType: String,
    val budgetID: Long
    //TO BE CHECKED
)
