package com.seenu.dev.android.smartstep.home.home_data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_history")
data class StepEntity(
    @PrimaryKey val date: String, // Format: YYYY-MM-DD
    val stepCount: Int,
    val activeSeconds: Long
)