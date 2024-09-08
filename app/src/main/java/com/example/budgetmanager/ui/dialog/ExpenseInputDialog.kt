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
import com.example.budgetmanager.ui.CustomValueFormatter
import com.example.budgetmanager.viewmodel.ExpenseViewModel
import java.time.LocalDate
import java.util.Locale

class ExpenseInputDialog(
    context: Context,
    private val expenseViewModel: ExpenseViewModel,
    private val budgetId: Long,
    private var expenseAction: ExpenseAction,
    private val expenseData: ExpenseData?
): Dialog(context, R.style.DialogStyle) {
    private var expenseTypeValue = ""
    private lateinit var closeBtn: ImageButton
    private lateinit var expenseInfo: EditText
    private lateinit var expenseTypeSpnr: Spinner
    private lateinit var expenseCost: EditText
    private lateinit var expenseAddBtn: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Configure the Dialog content and Layout
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.expense_input_dialog)
        window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        // Access layout view elements
        initViews()

        // Create an ArrayAdapter to be used for the Expense Type Spinner
        ArrayAdapter.createFromResource(
            this.context,
            R.array.expense_types,
            R.layout.custom_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            expenseTypeSpnr.adapter = adapter
        }

        // Define the behavior in case of selecting a Spinner element and default no selection
        expenseTypeSpnr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle the selected item
                expenseTypeValue = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Use Personal in case of no selection
                expenseTypeValue = "Personal"
            }
        }

        // Configure the dialog in case of Update
        if(expenseAction == ExpenseAction.Update) {
            configureUpdate()
        }

        // Close button handler
        closeBtn.setOnClickListener {
            dismiss()
        }

        // Add/Edit button handler
        expenseAddBtn.setOnClickListener {
            val infoIn = expenseInfo.text.toString()
            val costIn = expenseCost.text.toString()

            if(infoIn.isEmpty() || costIn.isEmpty())
            {
                // Missing data for some of the fields
                Toast.makeText(this.context, "Enter all fields", Toast.LENGTH_SHORT).show()
            } else if(CustomValueFormatter.checkValue(costIn)) {
                when(expenseAction) {
                    ExpenseAction.Add -> addExpense(infoIn, costIn)
                    ExpenseAction.Update -> updateExpense(infoIn, costIn)
                }

                expenseInfo.text.clear()
                expenseCost.text.clear()
            } else {
                // Report error with the input value
                Toast.makeText(this.context, "Invalid expense value, use format like 123.99", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViews() {
        closeBtn = findViewById(R.id.expenseCloseBtn)
        expenseInfo = findViewById(R.id.expenseInfoInput)
        expenseTypeSpnr  = findViewById(R.id.expenseTypeInput)
        expenseCost = findViewById(R.id.expenseValueInput)
        expenseAddBtn = findViewById(R.id.expenseAddBtn)
    }

    private fun configureUpdate() {
        // Set the value of Expense Type Spinner
        val typeOptions = context.resources.getStringArray(R.array.expense_types)
        expenseTypeValue = expenseData!!.expenseType
        when(val typeIndex = typeOptions.indexOf(expenseTypeValue)) {
            -1 -> expenseTypeSpnr.setSelection(typeOptions.indexOf("Personal"))
            else -> expenseTypeSpnr.setSelection(typeIndex)
        }

        // Set EditText fields to be edited
        val cost = String.format(Locale.US, "%.2f",expenseData.expenseVal)
        expenseCost.setText(cost)
        expenseInfo.setText(expenseData.expenseDescription)
        expenseTypeSpnr.setSelection(typeOptions.indexOf(expenseData.expenseType))

        // Switch to Update button
        expenseAddBtn.text = context.getString(R.string.update_expense)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addExpense(infoIn: String, costIn: String) {
        val expense = ExpenseData(expenseVal = costIn.toFloat(), expenseDescription = infoIn, expenseDate = LocalDate.now().toString(), expenseType = expenseTypeValue, budgetID = budgetId)

        // Add the new expense entry to DB
        expenseViewModel.addExpense(expense)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateExpense(infoIn: String, costIn: String) {
        if (expenseData != null) {
            // Update the fields of the edit expense
            expenseData.expenseDescription = infoIn
            expenseData.expenseVal = costIn.toFloat()
            expenseData.expenseType = expenseTypeValue
            expenseData.expenseDate = LocalDate.now().toString()

            // Update the expense in DB
            expenseViewModel.updateExpense(expenseData)

            // Close the dialog on update
            dismiss()
        }
    }
}