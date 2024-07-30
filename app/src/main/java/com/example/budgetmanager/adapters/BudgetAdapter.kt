package com.example.budgetmanager.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetmanager.ExpensesActivity
import com.example.budgetmanager.R
import com.example.budgetmanager.database.model.BudgetData
import com.example.budgetmanager.dialog.ConfirmationDialog
import com.example.budgetmanager.viewmodel.BudgetViewModel

class BudgetAdapter(
    private val context: Context,
    private val budgetViewModel: BudgetViewModel
): RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {
    private var dataset: List<BudgetData> = emptyList()

    //Inner ViewHolder class with all widgets
    class BudgetViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val budgetTitle: TextView = view.findViewById(R.id.budgetEntry)
        val budgetRemoveBtn: ImageButton = view.findViewById(R.id.budgetRemBtn)
        val budgetStatsBtn: ImageButton = view.findViewById(R.id.budgetStatsBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.budget_entry, parent, false)

        return BudgetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.budgetTitle.text = dataset[position].budgetName

        //Remove button handler
        holder.budgetRemoveBtn.setOnClickListener {
            val confirmationDialog = ConfirmationDialog(context, budgetViewModel, dataset[position])
            confirmationDialog.show()
        }

        //Item click handler
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ExpensesActivity::class.java)
            val extras = Bundle()

            //Configure the package to be sent to ExpensesActivity
            extras.putLong("ID", dataset[position].budgetId)
            extras.putString("Title", dataset[position].budgetName)
            extras.putFloat("Budget", dataset[position].budgetValue)
            extras.putFloat("Balance", dataset[position].balanceValue)
            intent.putExtras(extras)
            //Start the ExpensesActivity
            context.startActivity(intent)
        }

        //Stats button click handler
        holder.budgetStatsBtn.setOnClickListener {
            Toast.makeText(this.context, "Not yet implemented", Toast.LENGTH_LONG).show()
        }
    }

    fun updateDataset(dataSet: List<BudgetData>) {
        dataset = dataSet
        this.notifyDataSetChanged()
    }
}