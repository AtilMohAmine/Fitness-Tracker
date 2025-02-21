package com.atilmohamine.fitnesstracker.repository

import android.content.Context
import android.content.SharedPreferences
import com.atilmohamine.fitnesstracker.data.ImplementationMode

class SharedPreferencesRepositoryImpl(
    private val context: Context
) : SharedPreferencesRepository {

    private lateinit var sharedPreferences: SharedPreferences

    private val preferences by lazy { getSharedPreferences() }

    companion object {
        private const val OBJECTIVE_STEPS_KEY = "objective_steps"
        private const val IMPLEMENTATION_MODE = "implementation_mode"
    }

    private fun getSharedPreferences(): SharedPreferences {
        if (!::sharedPreferences.isInitialized) {
            sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        }
        return sharedPreferences
    }

    override fun saveObjectiveSteps(objectiveSteps: Int) {
        preferences.edit().putInt(OBJECTIVE_STEPS_KEY, objectiveSteps).apply()
    }

    override fun loadObjectiveSteps(): Int {
        return preferences.getInt(OBJECTIVE_STEPS_KEY, 8000)
    }

    override fun saveImplementationMode(implementationMode: ImplementationMode) {
        preferences.edit().putInt(IMPLEMENTATION_MODE, implementationMode.code).apply()
    }

    override fun loadImplementationMode(): Int {
        return preferences.getInt(IMPLEMENTATION_MODE, ImplementationMode.GOOGLE_FIT_API.code)
    }
}