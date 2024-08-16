package com.example.budgetmanager.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.R
import com.example.budgetmanager.database.model.ExpenseAction
import com.example.budgetmanager.database.model.ExpenseData
import com.example.budgetmanager.ui.dialog.ExpenseInputDialog
import com.example.budgetmanager.viewmodel.ExpenseViewModel
import java.util.Locale

class ExpenseAdapter(
    private val context: Context,
    private val expenseViewModel: ExpenseViewModel,
    private val budgetId: Long
): RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {
    private var dataset: List<ExpenseData> = emptyList()

    inner class ExpenseViewHolder(view: View): RecyclerView.ViewHolder(view){
        val expenseVal: TextView = view.findViewById(R.id.expenseValue)
        val expenseInfo: TextView = view.findViewById(R.id.expenseInfo)
        val expenseType: TextView = view.findViewById(R.id.expenseType)
        val expenseDate: TextView = view.findViewById(R.id.expenseDate)
        val expenseDeleteBtn: ImageButton = view.findViewById(R.id.expenseDeleteBtn)
        val expenseEditBtn: ImageButton = view.findViewById(R.id.expenseEditBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.expense_entry, parent, false)

        return ExpenseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.expenseVal.text = String.format(Locale.US,"%.2f",dataset[position].expenseVal)
        holder.expenseInfo.text = dataset[position].expenseDescription
        holder.expenseDate.text = dataset[position].expenseDate
        holder.expenseType.text = dataset[position].expenseType

        // Expense Remove button handler
        holder.expenseDeleteBtn.setOnClickListener {
            //Update balance
            val newBalance = expenseViewModel.balance.value?.plus(dataset[position].expenseVal)
            if (newBalance != null) {
                expenseViewModel.updateBalance(newBalance, budgetId)
            }

            // Delete the expense
            expenseViewModel.deleteExpense(dataset[position])
            this.notifyItemRangeChanged(position, dataset.size)
            this.notifyItemRemoved(position)
        }

        // Expense Edit button handler
        holder.expenseEditBtn.setOnClickListener {
            val expenseDialog = ExpenseInputDialog(this.context,
                expenseViewModel, budgetId, ExpenseAction.Update, dataset[position])
            expenseDialog.show()
        }
    }

    fun updateDataset(dataSet: List<ExpenseData>) {
        dataset = dataSet
        this.notifyDataSetChanged()
    }
}