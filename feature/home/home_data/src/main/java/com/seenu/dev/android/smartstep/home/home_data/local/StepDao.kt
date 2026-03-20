package com.seenu.dev.android.smartstep.home.home_data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    // Returns a Flow for the UI to observe continuously
    @Query("SELECT * FROM step_history WHERE date = :date")
    fun getStepsForDateFlow(date: String): Flow<StepEntity?>

    // Returns a single object for the Service to use when doing math
    @Query("SELECT * FROM step_history WHERE date = :date")
    suspend fun getStepsForDateSync(date: String): StepEntity?

    @Upsert
    suspend fun upsertStepData(stepEntity: StepEntity)

    @Query("DELETE FROM step_history WHERE date = :date")
    suspend fun deleteDate(date: String)
}