package com.seenu.dev.android.smartstep.home.home_data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_steps_entity")
data class DailyStepsEntity(
    @PrimaryKey val date: String,
    val steps: Int
)
