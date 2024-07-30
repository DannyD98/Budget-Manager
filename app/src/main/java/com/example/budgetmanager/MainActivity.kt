package com.example.budgetmanager

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.adapters.BudgetAdapter
import com.example.budgetmanager.decorator.MarginItemDecorator
import com.example.budgetmanager.dialog.BudgetInputDialog
import com.example.budgetmanager.viewmodel.BudgetViewModel

class MainActivity : AppCompatActivity() {
    private var budgetViewModel: BudgetViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //View model for Budget related data
        budgetViewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        //Get UI elements
        val recyclerView: RecyclerView = findViewById(R.id.BudgetsList)
        val addBudgetLogBtn: Button = findViewById(R.id.NewBudgetBtn)

        //Configure RecyclerView, Adapter and Decorator
        val budgetAdapter = BudgetAdapter(this, budgetViewModel!!)
        recyclerView.adapter = budgetAdapter
        recyclerView.addItemDecoration(MarginItemDecorator(32))

        //Observer for change of the data from the viewModel
        budgetViewModel?.budgets?.observe(this) { budgets ->
            budgets.let { budgetAdapter.updateDataset(it) }
            //Perform scroll to the latest item in the list
            if(budgets.isNotEmpty()) {
                recyclerView.smoothScrollToPosition(budgets.lastIndex)
            }
        }

        //Button Handlers
        addBudgetLogBtn.setOnClickListener {
            //Open a new dialog and process the Add budget
            val budgetInputDialog = BudgetInputDialog(this, budgetViewModel!!)
            budgetInputDialog.show()
        }
    }

    private fun initDataset()
    {
//        dataSet.add(BudgetData(budgetName = "January", budgetValue = 5000F))
//        dataSet.add(BudgetData(budgetName = "February", budgetValue = 4000F))
//        dataSet.add(BudgetData(budgetName = "March", budgetValue = 6700F))
//        dataSet.add(BudgetData(budgetName = "April", budgetValue = 7000F))
//        dataSet.add(BudgetData(budgetName = "May", budgetValue = 6500F))
//        dataSet.add(BudgetData(budgetName = "June", budgetValue = 7000F))
//        dataSet.add(BudgetData(budgetName = "July", budgetValue = 12000F))
    }
}