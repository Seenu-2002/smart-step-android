package com.seenu.dev.android.smartstep.home.home_data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.seenu.dev.android.smartstep.home.home_data.local.database.entities.DailyStepsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyStepsDao {

    @Upsert
    suspend fun upsertDailySteps(dailyStepsEntity: DailyStepsEntity)

    @Query("SELECT * FROM daily_steps_entity WHERE date = :date")
    fun getDailySteps(date: String): Flow<DailyStepsEntity?>

}