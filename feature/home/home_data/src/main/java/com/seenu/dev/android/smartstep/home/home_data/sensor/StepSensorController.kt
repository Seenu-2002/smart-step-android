package com.seenu.dev.android.smartstep.home.home_data.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class StepSensorController(
    context: Context,
    private val onStepDetected: (activeTimeDeltaSeconds: Long) -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

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
        if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
            val currentTimestampMs = System.currentTimeMillis()
            var deltaSeconds = 0L

            if (lastStepTimestampMs != 0L) {
                val timeDifferenceMs = currentTimestampMs - lastStepTimestampMs
                if (timeDifferenceMs < 10_000L) {
                    deltaSeconds = timeDifferenceMs / 1000L
                }
            }
            
            lastStepTimestampMs = currentTimestampMs

            onStepDetected(deltaSeconds)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}