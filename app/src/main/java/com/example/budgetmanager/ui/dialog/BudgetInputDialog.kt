package com.example.budgetmanager.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.budgetmanager.R
import com.example.budgetmanager.database.model.BudgetData
import com.example.budgetmanager.ui.CustomValueFormatter
import com.example.budgetmanager.viewmodel.BudgetViewModel

class BudgetInputDialog(context: Context, private val budgetViewModel: BudgetViewModel): Dialog(context,
    R.style.DialogStyle
) {
    private lateinit var closeBtn: ImageButton
    private lateinit var addBtn: Button
    private lateinit var budgetInName: EditText
    private lateinit var budgetInValue: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.budget_input_dialog)
        window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        // Access layout view elements
        initViews()

        // Close Button handler
        closeBtn.setOnClickListener {
            dismiss()
        }

        // Add button handler
        addBtn.setOnClickListener {
            val budgetInText = budgetInName.text.toString()
            val budgetInVal = budgetInValue.text.toString()

            if(budgetInText.isEmpty() || budgetInVal.isEmpty())
            {
                // Missing data for some of the fields
                Toast.makeText(this.context, "Enter all fields", Toast.LENGTH_SHORT).show()
            } else if(CustomValueFormatter.checkValue(budgetInVal)) {
                // Add a new budget to the dataSet in case the data is valid
                val entry = BudgetData(budgetName = budgetInText, budgetValue = budgetInVal.toFloat())
                budgetViewModel.addBudget(entry)

                // Clear EditText fields
                budgetInName.text.clear()
                budgetInValue.text.clear()
            } else {
                // Report error with the input value
                Toast.makeText(this.context, "Invalid budget value, use format like 123.99", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViews() {
        closeBtn = findViewById(R.id.budgDialogCloseBtn)
        addBtn = findViewById(R.id.DialogAddBtn)
        budgetInName = findViewById(R.id.expenseInfoInput)
        budgetInValue = findViewById(R.id.expenseValueInput)
    }
}