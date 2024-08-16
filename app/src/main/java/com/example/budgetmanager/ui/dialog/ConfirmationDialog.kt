package com.example.budgetmanager.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import com.example.budgetmanager.R
import com.example.budgetmanager.database.model.BudgetData
import com.example.budgetmanager.viewmodel.BudgetViewModel

class ConfirmationDialog(context: Context, private val budgetViewModel: BudgetViewModel, private val budgetData: BudgetData): Dialog(context,
    R.style.DialogStyle
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.confirmation_dialog)
        window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        val yesButton: Button = findViewById(R.id.confirmBtn)
        val cancelButton: Button = findViewById(R.id.cancelBtn)

        yesButton.setOnClickListener {
            // Delete the passed budget entry
            budgetViewModel.removeBudget(budgetData)

            // Close the dialog
            dismiss()
        }

        cancelButton.setOnClickListener {
            // Delete canceled - Close the dialog
            dismiss()
        }
    }
}