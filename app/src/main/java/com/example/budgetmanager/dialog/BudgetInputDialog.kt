package com.example.budgetmanager.dialog

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
import com.example.budgetmanager.viewmodel.BudgetViewModel

class BudgetInputDialog(context: Context, private val budgetViewModel: BudgetViewModel): Dialog(context,
    R.style.DialogStyle
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.budget_input_dialog)
        window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        val close: ImageButton = findViewById(R.id.budgDialogCloseBtn)
        val addBtn: Button = findViewById(R.id.DialogAddBtn)
        val budgetInName: EditText = findViewById(R.id.expenseInfoInput)
        val budgetInValue: EditText = findViewById(R.id.expenseValueInput)

        close.setOnClickListener {
            dismiss()
        }

        addBtn.setOnClickListener {
            val budgetInText = budgetInName.text.toString()
            val budgetInVal = budgetInValue.text.toString()

            if(budgetInText.isNotEmpty() &&
                budgetInVal.isNotEmpty())
            {
                //Add a new budget to the dataSet
                val entry = BudgetData(budgetName = budgetInText, budgetValue = budgetInVal.toFloat(), balanceValue = budgetInVal.toFloat())
                budgetViewModel.addBudget(entry)

                //Clear EditText fields
                budgetInName.text.clear()
                budgetInValue.text.clear()
            }
            else {
                Toast.makeText(this.context, "Enter all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}