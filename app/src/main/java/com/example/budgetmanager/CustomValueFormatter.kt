package com.example.budgetmanager

import android.icu.text.DecimalFormat
import com.github.mikephil.charting.formatter.ValueFormatter

class CustomValueFormatter : ValueFormatter() {
    private var decimalFormat: DecimalFormat

    init {
        decimalFormat = DecimalFormat("###,##,##0.00")
    }

    override fun getFormattedValue(value: Float): String {
        return decimalFormat.format(value) + " BGN"
    }
}