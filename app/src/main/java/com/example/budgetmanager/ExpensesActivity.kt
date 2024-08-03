package com.example.budgetmanager

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.adapters.ExpenseAdapter
import com.example.budgetmanager.database.model.ExpenseAction
import com.example.budgetmanager.decorator.MarginItemDecorator
import com.example.budgetmanager.dialog.ExpenseInputDialog
import com.example.budgetmanager.viewmodel.ExpenseViewModel
import java.util.Locale

class ExpensesActivity : AppCompatActivity() {
    private var expenseViewModel: ExpenseViewModel? = null
    private var budgetId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.budget_expenses_activity)

        //Access received data from MainActivity
        val intent = intent
        val extras = intent.extras

        //Parse Intent extras from Budget/MainActivity
        budgetId = extras?.getLong("ID")!!
        val budgetName = extras.getString("Title")
        val budgetValue = extras.getFloat("Budget")

        //Configure UI elements
        val recyclerView: RecyclerView = findViewById(R.id.expensesList)
        val addExpenseBtn: Button = findViewById(R.id.addExpenseBtn)
        val backButton: Button = findViewById(R.id.expensesBackBtn)
        val budgetTitle: TextView = findViewById(R.id.budgetLogName)
        val budgetVal: TextView = findViewById(R.id.budgetVal)
        val currentSpent: TextView = findViewById(R.id.SpentVal)
        val progressBar: ProgressBar = findViewById(R.id.expenseProgressBar)

        //ViewModel for the Expenses related data
        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        //Fetch all expenses from DB
        expenseViewModel!!.loadExpenses(budgetId)

        //Configure RecyclerView, Adapter and Decorator
        val expenseAdapter = ExpenseAdapter(this, expenseViewModel!!, budgetId)
        recyclerView.adapter = expenseAdapter
        recyclerView.addItemDecoration(MarginItemDecorator(32))

        //Observer for change of the Expenses data from the viewModel
        expenseViewModel?.expenses?.observe(this) { expenses ->
            expenses.let {
                expenseAdapter.updateDataset(it)
                //Perform scroll to the latest item in the list
                if(expenses.isNotEmpty()) {
                    recyclerView.smoothScrollToPosition(expenses.lastIndex)
                }
            }
        }

        //Observer for change of the Balance data
        expenseViewModel?.balance?.observe(this) { balance ->
            val spentValue = (budgetValue - balance)
            currentSpent.text = String.format(Locale.US,"%.2f", spentValue)
            progressBar.progress = ((spentValue * 100) / budgetValue).toInt()
        }

        //UI Updates
        if (budgetName != null) {
            budgetTitle.text = budgetName
            budgetVal.text = String.format(Locale.US,"%.2f", budgetValue)
        }

        //Add Expense Button handler
        addExpenseBtn.setOnClickListener {
            val expenseDialog = ExpenseInputDialog(this, expenseViewModel!!, budgetId, ExpenseAction.Add, null)
            expenseDialog.show()
        }

        //Back Button handler
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun initDataset()
    {
        //dataSet.add(TransactionData(32.5F, "Lunch", "10.05", "Expense"))
        //dataSet.add(TransactionData(10.52F, "Game", "07.05", "Expense"))
    }
}