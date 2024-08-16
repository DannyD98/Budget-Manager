package com.example.budgetmanager.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.budgetmanager.R
import com.example.budgetmanager.database.model.ExpenseAction
import com.example.budgetmanager.database.model.ExpenseData
import com.example.budgetmanager.viewmodel.ExpenseViewModel
import java.time.LocalDate

class ExpenseInputDialog(
    context: Context,
    private val expenseViewModel: ExpenseViewModel,
    private val budgetId: Long,
    private var expenseAction: ExpenseAction,
    private val expenseData: ExpenseData?
): Dialog(context, R.style.DialogStyle) {
    private var expenseType = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Configure the Dialog content and Layout
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.expense_input_dialog)
        window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        val closeBtn: ImageButton = findViewById(R.id.expenseCloseBtn)
        val expenseInfo: EditText = findViewById(R.id.expenseInfoInput)
        val expenseType: Spinner = findViewById(R.id.expenseTypeInput)
        val expenseCost: EditText = findViewById(R.id.expenseValueInput)
        val expenseAddBtn: Button = findViewById(R.id.expenseAddBtn)

        // Create an ArrayAdapter to be used for the Expense Type Spinner
        ArrayAdapter.createFromResource(
            this.context,
            R.array.expense_types,
            R.layout.custom_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            expenseType.adapter = adapter
        }

        // Define the behavior in case of selecting a Spinner element and default
        expenseType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle the selected item
                this@ExpenseInputDialog.expenseType = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Use Personal in case of no selection
                this@ExpenseInputDialog.expenseType = "Personal"
            }
        }

        // Configure the dialog in case of Update
        if(expenseAction == ExpenseAction.Update) {
            //Expense Type Spinner
            this.expenseType = expenseData!!.expenseType
            val typeOptions = context.resources.getStringArray(R.array.expense_types)

            when(val typeIndex = typeOptions.indexOf(this.expenseType)) {
                -1 -> expenseType.setSelection(typeOptions.indexOf("Personal"))
                else -> expenseType.setSelection(typeIndex)
            }

            // Set EditText fields to be edited
            expenseCost.setText(expenseData.expenseVal.toString())
            expenseInfo.setText(expenseData.expenseDescription)
            expenseType.setSelection(typeOptions.indexOf(expenseData.expenseType))

            // Switch to Update button
            expenseAddBtn.text = context.getString(R.string.update)
        }

        // Close the dialog handle
        closeBtn.setOnClickListener {
            dismiss()
        }

        // Behavior in case of Add button click
        expenseAddBtn.setOnClickListener {
            val infoIn = expenseInfo.text.toString()
            val costIn = expenseCost.text.toString()

            if(infoIn.isNotEmpty() &&
                costIn.isNotEmpty()
            )
            {
                when(expenseAction) {
                    ExpenseAction.Add -> addExpense(infoIn, costIn)
                    ExpenseAction.Update -> updateExpense(infoIn, costIn)
                }

                expenseInfo.text.clear()
                expenseCost.text.clear()
            }
            else {
                Toast.makeText(this.context, "Enter all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addExpense(infoIn: String, costIn: String) {
        val expense = ExpenseData(expenseVal = costIn.toFloat(), expenseDescription = infoIn, expenseDate = LocalDate.now().toString(), expenseType = expenseType, budgetID = budgetId)

        // Add the new expense entry to DB
        expenseViewModel.addExpense(expense)

        // Update balance
        val newBalance = expenseViewModel.balance.value?.minus(expense.expenseVal)
        if (newBalance != null) {
            expenseViewModel.updateBalance(newBalance, budgetId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateExpense(infoIn: String, costIn: String) {
        if (expenseData != null) {
            val newBalance: Float
            val expenseDiff = costIn.toFloat() - expenseData.expenseVal

            // Update the fields of the edit expense
            expenseData.expenseDescription = infoIn
            expenseData.expenseVal = costIn.toFloat()
            expenseData.expenseType = expenseType
            expenseData.expenseDate = LocalDate.now().toString()

            // Update the expense in DB
            expenseViewModel.updateExpense(expenseData)

            // Update balance
            newBalance = expenseViewModel.balance.value?.minus(expenseDiff)!!
            expenseViewModel.updateBalance(newBalance, budgetId)

            // Close the dialog on update
            dismiss()
        }
    }
}