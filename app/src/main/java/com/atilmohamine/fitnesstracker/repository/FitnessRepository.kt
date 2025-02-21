package com.atilmohamine.fitnesstracker.repository

import com.atilmohamine.fitnesstracker.model.DailyFitnessModel
import com.atilmohamine.fitnesstracker.model.WeeklyFitnessModel

interface FitnessRepository {
    suspend fun getDailyFitnessData(): DailyFitnessModel
    suspend fun getWeeklyFitnessData(): WeeklyFitnessModel
}