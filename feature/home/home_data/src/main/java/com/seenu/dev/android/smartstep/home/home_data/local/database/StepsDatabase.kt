package com.seenu.dev.android.smartstep.home.home_data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seenu.dev.android.smartstep.home.home_data.local.database.entities.DailyStepsEntity

@Database(entities = [DailyStepsEntity::class], version = 1)
abstract class StepsDatabase : RoomDatabase() {
    abstract val dailyStepsDao: DailyStepsDao
}
