package com.atilmohamine.fitnesstracker.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.atilmohamine.fitnesstracker.model.DailyFitnessModel
import com.atilmohamine.fitnesstracker.model.WeeklyFitnessModel
import com.atilmohamine.fitnesstracker.repository.FitnessRepository
import com.atilmohamine.fitnesstracker.repository.FitnessRepositoryImpl
import com.atilmohamine.fitnesstracker.repository.SharedPreferencesRepository
import com.atilmohamine.fitnesstracker.repository.SharedPreferencesRepositoryImpl

class FitnessViewModel: ViewModel() {

    val fitnessRepo: FitnessRepository = FitnessRepositoryImpl()
    val sharedPreferencesRepo: SharedPreferencesRepository = SharedPreferencesRepositoryImpl()

    fun getDailyFitnessData(context: Context): LiveData<DailyFitnessModel> {
        var dailyFitnessLiveData = fitnessRepo.getDailyFitnessData(context)
        return dailyFitnessLiveData
    }

    fun getWeeklyFitnessData(context: Context): LiveData<WeeklyFitnessModel> {
        var weeklyFitnessLiveData = fitnessRepo.getWeeklyFitnessData(context)
        return weeklyFitnessLiveData
    }

    fun saveObjectiveSteps(context: Context, objectiveSteps: Int) {
        sharedPreferencesRepo.saveObjectiveSteps(context, objectiveSteps)
    }

    fun loadObjectiveSteps(context: Context): Int {
        return sharedPreferencesRepo.loadObjectiveSteps(context)
    }

}