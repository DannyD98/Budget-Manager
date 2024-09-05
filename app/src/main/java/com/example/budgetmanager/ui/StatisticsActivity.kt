package com.example.budgetmanager.ui

import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.budgetmanager.R
import com.example.budgetmanager.viewmodel.StatisticsViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.Locale

class StatisticsActivity : AppCompatActivity() {
    private lateinit var budgetTitle: TextView
    private lateinit var backBtn: Button
    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        // Access layout view elements
        initViews()

        // Process extras coming with the intent that started the Activity
        val extras = intent.extras
        val budgetId = extras?.getLong("ID") ?: 0
        val budgetTitle = extras?.getString("Title") ?: "Unknown"

        val statisticsViewModel = ViewModelProvider(this)[StatisticsViewModel::class.java]

        // Update the Budget title with the Extra value coming from Main Activity
        this.budgetTitle.text = budgetTitle

        // Configure the UI parameters of the PieChart
        configurePieChart()

        // Fetch data through ViewModel
        statisticsViewModel.getExpenseSumsByBudget(budgetId)

        statisticsViewModel.totalExpenses.observe(this) { total ->
            //Update PieChart center text
            val totalSum = total ?: 0f
            val expenseCenterText = String.format(Locale.US, "%.2f", totalSum) + " BGN\nTotal expenses"
            pieChart.centerText = expenseCenterText
        }

        // Observe for data changes of the ViewModel LiveData containing expense sums by type
        statisticsViewModel.expensesSum.observe(this) { sums ->
            updatePieChartData(sums)
        }

        // Back button click handler
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun initViews() {
        backBtn = findViewById(R.id.statisticsBackBtn)
        budgetTitle = findViewById(R.id.statisticsBudgetName)
        pieChart = findViewById(R.id.statisticsPieChart)
    }

    private fun configurePieChart() {
        val orientation = resources.configuration.orientation

        // Configuration for the center of the PieChart
        pieChart.setCenterTextSize(16f)
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        pieChart.holeRadius = 60f
        pieChart.setHoleColor(0x00000000)

        // Configure the Pie Labels for the different expense types - currently disabled
        pieChart.setEntryLabelColor(resources.getColor(R.color.black, theme))
        pieChart.setDrawEntryLabels(false)

        pieChart.description.isEnabled = false

        // Configure the PieChart Legend
        pieChart.legend.textSize = 16f
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            pieChart.legend.xEntrySpace = 32f
            pieChart.legend.yEntrySpace = 8f
            pieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            pieChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
            pieChart.legend.isWordWrapEnabled = true
        } else {
            pieChart.legend.yEntrySpace = 4f
            pieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
            pieChart.legend.isWordWrapEnabled = false
        }

        pieChart.animateY(1000)
    }

    private fun updatePieChartData(sum: Map<String, Float>) {
        val customColors: List<Int> = resources.getIntArray(R.array.custom_colors).toList()
        val entries = ArrayList<PieEntry>()

        // Add each expense category and it's sum as PieEntry
        sum.forEach { (label, value) ->
            entries.add(PieEntry(value, label))
        }

        // Create and configure the dataset based on the read data
        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.valueTextColor = resources.getColor(R.color.black, theme)
        pieDataSet.sliceSpace = 4f
        pieDataSet.valueTextSize = 12f
        pieDataSet.colors = customColors
        pieDataSet.selectionShift = 16f

        // Set the PieData and it's format
        pieChart.data = PieData(pieDataSet)
        pieChart.data.setValueFormatter(CustomValueFormatter())
    }
}
