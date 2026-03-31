package com.seenu.dev.android.smartstep.home.home_data.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

// TODO: REBOOT issue. Refer: Claude chat
class StepSensorDataSource constructor(
    context: Context,
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private val _steps = MutableStateFlow(SensorData())
    val steps: StateFlow<SensorData> = _steps

    private var lastStepTimestampMs: Long = 0L
    private var cumulativeActiveSeconds: Long = 0L

    fun startListening() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
        // Reset counters at start of session
        lastStepTimestampMs = 0L
        cumulativeActiveSeconds = 0L
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
        lastStepTimestampMs = 0L
        cumulativeActiveSeconds = 0L
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val currentTimestampMs = System.currentTimeMillis()
            val deltaSeconds = if (lastStepTimestampMs != 0L) {
                val timeDifferenceMs = currentTimestampMs - lastStepTimestampMs
                // Remove 10s cutoff; accumulate all time between steps as active time
                (timeDifferenceMs / 1000L)
            } else 0L

            lastStepTimestampMs = currentTimestampMs

            // Accumulate total active seconds
            if (deltaSeconds > 0) {
                cumulativeActiveSeconds += deltaSeconds
            }

            _steps.update {
                it.copy(
                    totalSteps = event.values[0].toInt(),
                    // Emit cumulative active seconds for the session/day
                    activeSeconds = cumulativeActiveSeconds
                )
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}