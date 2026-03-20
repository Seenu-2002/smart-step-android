package com.seenu.dev.android.smartstep.home.home_data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StepEntity::class], version = 1, exportSchema = false)
abstract class StepDatabase : RoomDatabase() {
    abstract val stepDao: StepDao
}