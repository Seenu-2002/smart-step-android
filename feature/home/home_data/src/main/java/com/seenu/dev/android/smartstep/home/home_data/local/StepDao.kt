package com.seenu.dev.android.smartstep.home.home_data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.seenu.dev.android.smartstep.home.home_domain.model.StepsPerDay
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    // Returns a Flow for the UI to observe continuously
    @Query("SELECT * FROM step_history WHERE date = :date")
    fun getStepsForDateFlow(date: String): Flow<DailyStepEntity?>

    // Returns a single object for the Service to use when doing math
    @Query("SELECT * FROM step_history WHERE date = :date")
    suspend fun getStepsForDateSync(date: String): DailyStepEntity?

    @Upsert
    suspend fun upsertStepData(dailyStepEntity: DailyStepEntity)

    @Query("UPDATE step_history SET stepGoal = :newStepGoal WHERE date = :date")
    suspend fun updateStepGoalForDate(date: String, newStepGoal: Int)

    @Query("DELETE FROM step_history WHERE date = :date")
    suspend fun deleteDate(date: String)

    @Query("SELECT date, stepCount AS steps, stepGoal as goal FROM step_history WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getStepsForDateRangeFlow(startDate: String, endDate: String): Flow<List<StepsPerDay>>
}