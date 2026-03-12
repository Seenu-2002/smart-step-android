package com.seenu.dev.android.smartstep.home.home_data.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StepSensorDataSource(
    context: Context,
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private val _steps = MutableStateFlow(SensorData())
    val steps: StateFlow<SensorData> = _steps

    private var lastStepTimestampMs: Long = 0L

    fun startListening() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
        lastStepTimestampMs = 0L
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val currentTimestampMs = System.currentTimeMillis()
            var deltaSeconds = 0L

            if (lastStepTimestampMs != 0L) {
                val timeDifferenceMs = currentTimestampMs - lastStepTimestampMs
                if (timeDifferenceMs < 10_000L) {
                    deltaSeconds = timeDifferenceMs / 1000L
                }
            }
            
            lastStepTimestampMs = currentTimestampMs

            _steps.update {
                it.copy(
                    totalSteps = event.values[0].toInt(),
                    activeSeconds = deltaSeconds
                )
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}