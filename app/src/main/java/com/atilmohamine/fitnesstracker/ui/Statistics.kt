package com.atilmohamine.fitnesstracker.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.atilmohamine.fitnesstracker.R
import com.atilmohamine.fitnesstracker.model.DailyFitnessModel
import com.atilmohamine.fitnesstracker.model.WeeklyFitnessModel
import com.atilmohamine.fitnesstracker.utils.DayAxisValueFormatter
import com.atilmohamine.fitnesstracker.viewmodel.FitnessViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*


class Statistics : Fragment() {

    private lateinit var progressChart: BarChart

    private val fitnessViewModel: FitnessViewModel by viewModels()
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_statistics, container, false)

        progressChart = rootView.findViewById(R.id.progress_chart)

        fitnessViewModel.getWeeklyFitnessData(rootView.context).observe(viewLifecycleOwner, Observer { WeeklyFitness->
            loadChart(WeeklyFitness)
        })

        return rootView
    }

    private fun loadChart(WeeklyFitness: WeeklyFitnessModel) {
        progressChart.description.isEnabled = false
        progressChart.setTouchEnabled(false)
        progressChart.setDrawGridBackground(false)

        val caloriesDataSet = BarDataSet(mutableListOf(), "Calories")
        val stepsDataSet = BarDataSet(mutableListOf(), "Steps")
        stepsDataSet.color = ContextCompat.getColor(rootView.context, R.color.calories)
        val distanceDataSet = LineDataSet(mutableListOf(), "Distance")

        WeeklyFitness.dailyFitnessList.forEachIndexed { index, fitnessData ->
            val caloriesEntry = BarEntry(index.toFloat(), fitnessData.caloriesBurned.toFloat())
            val stepsEntry = BarEntry(index.toFloat(), fitnessData.stepCount.toFloat())
            val distanceEntry = BarEntry(index.toFloat(), fitnessData.distance)

            caloriesDataSet.addEntry(caloriesEntry)
            stepsDataSet.addEntry(stepsEntry)
            distanceDataSet.addEntry(distanceEntry)
        }

        val data = BarData()
        //data.addDataSet(caloriesDataSet)
        data.addDataSet(stepsDataSet)
        //data.addDataSet(distanceDataSet)

        progressChart.data = data

        // Set X-axis properties
        val xAxis = progressChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //xAxis.labelCount = 7
        xAxis.valueFormatter = DayAxisValueFormatter(progressChart)

        // Set Y-axis properties
        val yAxisLeft = progressChart.axisLeft
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.isEnabled = false
        yAxisLeft.axisMinimum = 0f

        val yAxisRight = progressChart.axisRight
        yAxisRight.isEnabled = false
        yAxisLeft.setDrawGridLines(false)

        val barData = progressChart.barData
        barData.barWidth = 0.6f

        progressChart.legend.isEnabled = false

        progressChart.notifyDataSetChanged()
        progressChart.invalidate()
    }

}