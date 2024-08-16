package com.example.budgetmanager.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.R
import com.example.budgetmanager.ui.adapters.BudgetAdapter
import com.example.budgetmanager.ui.decorator.MarginItemDecorator
import com.example.budgetmanager.ui.dialog.BudgetInputDialog
import com.example.budgetmanager.viewmodel.BudgetViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var budgetRecyclerView: RecyclerView
    private lateinit var addBudgetBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // View model for Budget related data
        budgetViewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        // Access layout view elements
        initViews()

        // Configure RecyclerView, Adapter and Decorator
        val budgetAdapter = BudgetAdapter(this, budgetViewModel)
        budgetRecyclerView.adapter = budgetAdapter
        budgetRecyclerView.addItemDecoration(MarginItemDecorator(32))

        // Observer for change of the data from the ViewModel containing budgets
        budgetViewModel.budgets.observe(this) { budgets ->
            budgets.let { budgetAdapter.updateDataset(it) }
            //Perform scroll to the latest item in the list
            if(budgets.isNotEmpty()) {
                budgetRecyclerView.smoothScrollToPosition(budgets.lastIndex)
            }
        }

        // Add Button click Handler
        addBudgetBtn.setOnClickListener {
            // Open a new dialog and process the Add budget
            val budgetInputDialog = BudgetInputDialog(this, budgetViewModel)
            budgetInputDialog.show()
        }
    }

    private fun initViews() {
        budgetRecyclerView = findViewById(R.id.BudgetsList)
        addBudgetBtn = findViewById(R.id.NewBudgetBtn)
    }
}