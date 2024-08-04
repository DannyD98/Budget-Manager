package com.example.budgetmanager

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.budgetmanager.viewmodel.StatisticsViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
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

        //Access layout view elements
        initViews()

        //Process extras coming with the intent that started the Activity
        val extras = intent.extras
        if (extras != null) {
            budgetId = extras.getLong("ID")
            budgetTitle = extras.getString("Title")!!
            spent = extras.getFloat("Spent")
        }

        //Update the Budget title from the Extra value
        budgetName.text = budgetTitle

        configurePieChart()

        val statisticsViewModel = ViewModelProvider(this)[StatisticsViewModel::class.java]
        statisticsViewModel.getExpensesSumByType(budgetId)

        statisticsViewModel.expensesSum.observe(this) { sum ->
            updatePieChartData(sum)
        }

        // Back button click handler
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun updatePieChartData(sum: Map<String, Float>?) {
        val entries = ArrayList<PieEntry>()

        //Add each of the expenses categories and their sums
        sum?.forEach { (label, value) ->
            entries.add(PieEntry(value, label))
        }

        //Create and configure the dataset based on the read data
        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.valueTextColor = resources.getColor(R.color.VeryDarkGray, theme)
        pieDataSet.sliceSpace = 3f
        pieDataSet.valueTextSize = 16f
        pieDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        pieDataSet.selectionShift = 12f

        pieChart.data = PieData(pieDataSet)
        pieChart.data.setValueFormatter(CustomValueFormatter())
    }

    private fun initViews() {
        backBtn = findViewById(R.id.statisticsBackBtn)
        budgetName = findViewById(R.id.statisticsBudgetName)
        pieChart = findViewById(R.id.statisticsPieChart)
    }

    private fun configurePieChart() {
        // Configuration for the center of the PieChart
        pieChart.setCenterTextSize(16f)
        pieChart.centerText = String.format(Locale.US, "%.2f", spent) + " BGN\nTotal expenses"
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        pieChart.holeRadius = 60f
        pieChart.setHoleColor(0x00000000)

        pieChart.setEntryLabelColor(resources.getColor(R.color.black, theme))
        pieChart.animateY(1000)

        pieChart.description.isEnabled = false
        pieChart.legend.xEntrySpace = 24f
        pieChart.invalidate()
    }
}