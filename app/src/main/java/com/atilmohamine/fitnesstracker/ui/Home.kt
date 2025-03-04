package com.atilmohamine.fitnesstracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.atilmohamine.fitnesstracker.R
import com.atilmohamine.fitnesstracker.viewmodel.FitnessViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class Home : Fragment() {

    private lateinit var textViewSteps: TextView
    private lateinit var textViewStepsBig: TextView
    private lateinit var textViewCalories: TextView
    private lateinit var textViewDistance: TextView
    private lateinit var stepsProgressBar: ProgressBar

    private val fitnessViewModel: FitnessViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        requireContext()
        textViewSteps = rootView.findViewById(R.id.steps)
        textViewStepsBig = rootView.findViewById(R.id.steps_big)
        textViewCalories = rootView.findViewById(R.id.burned_calories)
        textViewDistance = rootView.findViewById(R.id.distance)
        stepsProgressBar = rootView.findViewById(R.id.stepsProgressBar)
        stepsProgressBar.max = fitnessViewModel.loadObjectiveSteps()

        lifecycleScope.launch {
            fitnessViewModel.getDailyFitnessData()
        }
        fitnessViewModel.dailyData.observe(viewLifecycleOwner) {
            textViewSteps.text = "${it.stepCount}"
            textViewStepsBig.text = "${it.stepCount}"
            textViewCalories.text = "${it.caloriesBurned}"
            textViewDistance.text = String.format(Locale.US, " % .2f", it.distance)
            stepsProgressBar.progress = it.stepCount
        }

        return rootView
    }

}