package com.example.budgetmanager

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.budgetmanager.viewmodel.StatisticsViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.Locale

class StatisticsActivity : AppCompatActivity() {
    private lateinit var backBtn: Button
    private lateinit var budgetName: TextView
    private lateinit var pieChart: PieChart
    private var budgetId: Long = 0
    private var budgetTitle: String = "Unknown"
    private var spent: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        // Access layout view elements
        initViews()

        // Process extras coming with the intent that started the Activity
        val extras = intent.extras
        if (extras != null) {
            budgetId = extras.getLong("ID")
            budgetTitle = extras.getString("Title")!!
            spent = extras.getFloat("Spent")
        }

        val statisticsViewModel = ViewModelProvider(this)[StatisticsViewModel::class.java]

        // Update the Budget title with the Extra value coming from Main Activity
        budgetName.text = budgetTitle

        // Configure the UI parameters of the PieChart
        configurePieChart()

        // Fetch data through ViewModel
        statisticsViewModel.getExpensesSumByType(budgetId)

        // Observe for data changes of the ViewModel LiveData containing expense sums by type
        statisticsViewModel.expensesSum.observe(this) { sum ->
            updatePieChartData(sum)
        }

        // Back button click handler
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun initViews() {
        backBtn = findViewById(R.id.statisticsBackBtn)
        budgetName = findViewById(R.id.statisticsBudgetName)
        pieChart = findViewById(R.id.statisticsPieChart)
    }

    private fun configurePieChart() {
        val expenseCenterText = String.format(Locale.US, "%.2f", spent) + " BGN\nTotal expenses"

        // Configuration for the center of the PieChart
        pieChart.setCenterTextSize(16f)
        pieChart.centerText = expenseCenterText
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        pieChart.holeRadius = 60f
        pieChart.setHoleColor(0x00000000)

        // Configure the Pie Labels for the different expense types - currently disabled
        pieChart.setEntryLabelColor(resources.getColor(R.color.black, theme))
        pieChart.setDrawEntryLabels(false)

        pieChart.description.isEnabled = false

        // Configure the PieChart Legend
        pieChart.legend.textSize = 16f
        pieChart.legend.xEntrySpace = 32f
        pieChart.legend.yEntrySpace = 16f
        pieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        pieChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        pieChart.legend.isWordWrapEnabled = true

        pieChart.animateY(1000)
        pieChart.invalidate()
    }

    private fun updatePieChartData(sum: Map<String, Float>?) {
        val customColors: List<Int> = resources.getIntArray(R.array.custom_colors).toList()
        val entries = ArrayList<PieEntry>()

        // Add each expense category and it's sum as PieEntry
        sum?.forEach { (label, value) ->
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
