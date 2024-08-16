package com.example.budgetmanager.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.R
import com.example.budgetmanager.ui.adapters.ExpenseAdapter
import com.example.budgetmanager.database.model.ExpenseAction
import com.example.budgetmanager.ui.decorator.MarginItemDecorator
import com.example.budgetmanager.ui.dialog.ExpenseInputDialog
import com.example.budgetmanager.viewmodel.ExpenseViewModel
import java.util.Locale

class ExpenseActivity : AppCompatActivity() {
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var addExpenseBtn: Button
    private lateinit var backBtn: Button
    private lateinit var budgetTitle: TextView
    private lateinit var budgetVal: TextView
    private lateinit var currentSpentVal: TextView
    private lateinit var spentProgressBar: ProgressBar
    private var budgetId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        // Access received data from MainActivity
        val extras = intent.extras

        // Parse Intent extras from Budget/MainActivity
        budgetId = extras?.getLong("ID")!!
        val budgetName = extras.getString("Title")
        val budgetValue = extras.getFloat("Budget")

        // ViewModel for the Expenses related data
        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        // Access layout view elements
        initViews()

        // Fetch all expenses from DB
        expenseViewModel.loadExpenses(budgetId)

        // Configure RecyclerView, Adapter and Decorator
        val expenseAdapter = ExpenseAdapter(this, expenseViewModel, budgetId)
        expenseRecyclerView.adapter = expenseAdapter
        expenseRecyclerView.addItemDecoration(MarginItemDecorator(32))

        // Observer for change of the Expenses data from the viewModel
        expenseViewModel.expenses.observe(this) { expenses ->
            expenses.let {
                expenseAdapter.updateDataset(it)
                //Perform scroll to the latest item in the list
                if(expenses.isNotEmpty()) {
                    expenseRecyclerView.smoothScrollToPosition(expenses.lastIndex)
                }
            }
        }

        // Observer for change of the Balance data
        expenseViewModel.balance.observe(this) { balance ->
            val spentValue = (budgetValue - balance)
            currentSpentVal.text = String.format(Locale.US,"%.2f", spentValue)
            spentProgressBar.progress = ((spentValue * 100) / budgetValue).toInt()
        }

        // UI Updates
        if (budgetName != null) {
            budgetTitle.text = budgetName
            budgetVal.text = String.format(Locale.US,"%.2f", budgetValue)
        }

        // Add Expense Button handler
        addExpenseBtn.setOnClickListener {
            val expenseDialog = ExpenseInputDialog(this, expenseViewModel, budgetId, ExpenseAction.Add, null)
            expenseDialog.show()
        }

        // Back Button handler
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun initViews() {
        expenseRecyclerView = findViewById(R.id.expensesList)
        addExpenseBtn = findViewById(R.id.addExpenseBtn)
        backBtn = findViewById(R.id.expensesBackBtn)
        budgetTitle = findViewById(R.id.budgetLogName)
        budgetVal = findViewById(R.id.budgetVal)
        currentSpentVal = findViewById(R.id.SpentVal)
        spentProgressBar = findViewById(R.id.expenseProgressBar)
    }
}