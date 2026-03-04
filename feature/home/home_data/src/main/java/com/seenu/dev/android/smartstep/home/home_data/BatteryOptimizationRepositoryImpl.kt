package com.seenu.dev.android.smartstep.home.home_data

import android.content.Context
import android.os.PowerManager
import com.seenu.dev.android.smartstep.home.home_domain.BatteryOptimizationRepository

class BatteryOptimizationRepositoryImpl(
    private val context: Context
) : BatteryOptimizationRepository {

    override fun isIgnoringBatteryOptimizations(): Boolean {
        val powerManager =
            context.getSystemService(Context.POWER_SERVICE) as PowerManager

        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }
}