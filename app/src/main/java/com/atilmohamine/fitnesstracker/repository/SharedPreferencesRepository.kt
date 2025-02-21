package com.atilmohamine.fitnesstracker.repository

import com.atilmohamine.fitnesstracker.data.ImplementationMode

interface SharedPreferencesRepository {
    fun saveObjectiveSteps(objectiveSteps: Int)
    fun loadObjectiveSteps(): Int
    fun saveImplementationMode(implementationMode: ImplementationMode)
    fun loadImplementationMode(): Int
}