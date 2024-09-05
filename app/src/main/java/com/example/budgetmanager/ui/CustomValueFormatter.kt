package com.example.budgetmanager.ui

import android.icu.text.DecimalFormat
import com.github.mikephil.charting.formatter.ValueFormatter

class CustomValueFormatter : ValueFormatter() {
    private var decimalFormat: DecimalFormat = DecimalFormat("###,##,##0.00")

    companion object {
        fun checkValue(value: String) : Boolean {
            val regex = "[0-9]+(\\.\\d{2})?".toRegex()

            return regex.matches(value)
        }
    }
    override fun getFormattedValue(value: Float): String {
        return decimalFormat.format(value) + " BGN"
    }
}