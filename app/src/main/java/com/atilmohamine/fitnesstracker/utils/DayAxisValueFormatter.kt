package com.atilmohamine.fitnesstracker.utils

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class DayAxisValueFormatter(private val chart: BarChart) : ValueFormatter() {

    private val dateFormat = SimpleDateFormat("EEE", Locale.ENGLISH)

    override fun getFormattedValue(value: Float): String {
        val daysAgo = chart.data.dataSets[0].entryCount - value.toInt() - 1
        return when (daysAgo) {
            0 -> "Today"
            1 -> "Yesterday"
            else -> {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
                dateFormat.format(calendar.time)
            }
        }
    }
}