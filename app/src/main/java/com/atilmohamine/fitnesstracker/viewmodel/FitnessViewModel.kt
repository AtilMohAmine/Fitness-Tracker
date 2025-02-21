package com.atilmohamine.fitnesstracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atilmohamine.fitnesstracker.data.ImplementationMode
import com.atilmohamine.fitnesstracker.model.DailyFitnessModel
import com.atilmohamine.fitnesstracker.model.WeeklyFitnessModel
import com.atilmohamine.fitnesstracker.repository.FitnessRepoHealthConnectImpl
import com.atilmohamine.fitnesstracker.repository.FitnessRepository
import com.atilmohamine.fitnesstracker.repository.FitnessRepositoryImpl
import com.atilmohamine.fitnesstracker.repository.SharedPreferencesRepository
import com.atilmohamine.fitnesstracker.repository.SharedPreferencesRepositoryImpl
import com.atilmohamine.fitnesstracker.ui.BaseApplication
import kotlinx.coroutines.launch

class FitnessViewModel(application: Application) : AndroidViewModel(application) {

    private val healthConnectManager by lazy { (application as BaseApplication).healthConnectManager }

    private val fitnessRepo: FitnessRepository by lazy {
        when (sharedPreferencesRepo.loadImplementationMode()) {
            ImplementationMode.GOOGLE_HEALTH_CONNECT.code -> FitnessRepoHealthConnectImpl(
                healthConnectManager
            )
            else -> FitnessRepositoryImpl(application)
        }
    }

    val sharedPreferencesRepo: SharedPreferencesRepository =
        SharedPreferencesRepositoryImpl(application)

    private val _dailyData = MutableLiveData<DailyFitnessModel>()
    val dailyData: LiveData<DailyFitnessModel> get() = _dailyData

    private val _weeklyData = MutableLiveData<WeeklyFitnessModel>()
    val weeklyData: LiveData<WeeklyFitnessModel> get() = _weeklyData

    fun getDailyFitnessData() {
        viewModelScope.launch {
                _dailyData.postValue(fitnessRepo.getDailyFitnessData())
        }
    }

    fun getWeeklyFitnessData() {
        viewModelScope.launch {
                _weeklyData.postValue(fitnessRepo.getWeeklyFitnessData())
        }
    }

    fun saveObjectiveSteps(objectiveSteps: Int) {
        sharedPreferencesRepo.saveObjectiveSteps(objectiveSteps)
    }

    fun loadObjectiveSteps(): Int {
        return sharedPreferencesRepo.loadObjectiveSteps()
    }

    fun saveImplementationMode(implementationMode: ImplementationMode) {
        sharedPreferencesRepo.saveImplementationMode(implementationMode)
    }

    fun loadImplementationMode(): Int {
        return sharedPreferencesRepo.loadImplementationMode()
    }
}