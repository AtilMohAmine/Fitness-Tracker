package com.atilmohamine.fitnesstracker.repository

import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.time.TimeRangeFilter
import com.atilmohamine.fitnesstracker.data.HealthConnectManager
import com.atilmohamine.fitnesstracker.model.DailyFitnessModel
import com.atilmohamine.fitnesstracker.model.WeeklyFitnessModel
import java.time.Instant
import java.time.ZoneOffset

class FitnessRepoHealthConnectImpl(
    private val healthConnectManager: HealthConnectManager
): FitnessRepository {

    override suspend fun getDailyFitnessData(): DailyFitnessModel {
        val startTime = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate().atStartOfDay()
            .toInstant(ZoneOffset.UTC)
        val endTime = Instant.now()

        val timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
        return getFitnessDataForTimeRange(timeRangeFilter).first()
    }

    override suspend fun getWeeklyFitnessData(): WeeklyFitnessModel {
        val startTime = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate()
            .minusDays(7)
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC)
        val endTime = Instant.now()

        val timeRangeFilter = TimeRangeFilter.between(startTime, endTime)

        val weeklyRecords = getFitnessDataForTimeRange(timeRangeFilter)
        return WeeklyFitnessModel(dailyFitnessList = weeklyRecords)
    }

    private suspend fun getFitnessDataForTimeRange(timeRangeFilter: TimeRangeFilter): List<DailyFitnessModel> {
        return healthConnectManager.readAggregatedData(
            timeRangeFilter = timeRangeFilter,
            metrics = setOf(
                StepsRecord.COUNT_TOTAL,
                TotalCaloriesBurnedRecord.ENERGY_TOTAL,
                DistanceRecord.DISTANCE_TOTAL
            )
        ).map {
            DailyFitnessModel(
                stepCount = (it.result[StepsRecord.COUNT_TOTAL] ?: 0).toInt(),
                caloriesBurned = (it.result[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories
                    ?: 0).toInt(),
                distance = (it.result[DistanceRecord.DISTANCE_TOTAL]?.inMeters ?: 0).toFloat() / 1000
            )
        }
    }
}
