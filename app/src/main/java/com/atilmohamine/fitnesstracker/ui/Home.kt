package com.atilmohamine.fitnesstracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.atilmohamine.fitnesstracker.R
import com.atilmohamine.fitnesstracker.viewmodel.FitnessViewModel

class Home : Fragment() {

    private lateinit var textViewSteps: TextView
    private lateinit var textViewStepsBig: TextView
    private lateinit var textViewCalories: TextView
    private lateinit var textViewDistance: TextView
    private lateinit var stepsProgressBar: ProgressBar

    private val fitnessViewModel: FitnessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        requireContext()
        textViewSteps = rootView.findViewById(R.id.steps)
        textViewStepsBig = rootView.findViewById(R.id.steps_big)
        textViewCalories = rootView.findViewById(R.id.burned_calories)
        textViewDistance = rootView.findViewById(R.id.distance)
        stepsProgressBar = rootView.findViewById(R.id.stepsProgressBar)
        stepsProgressBar.max = fitnessViewModel.loadObjectiveSteps(rootView.context)

        fitnessViewModel.getDailyFitnessData(rootView.context).observe(viewLifecycleOwner, Observer { DailyFitness->
            textViewSteps.text = DailyFitness.stepCount.toString()
            textViewStepsBig.text = DailyFitness.stepCount.toString()
            textViewCalories.text = DailyFitness.caloriesBurned.toString()
            textViewDistance.text = String.format("%.2f", DailyFitness.distance)
            stepsProgressBar.progress = DailyFitness.stepCount
        })

        return rootView
    }

}