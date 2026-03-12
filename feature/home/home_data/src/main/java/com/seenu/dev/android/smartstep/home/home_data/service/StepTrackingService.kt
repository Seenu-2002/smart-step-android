package com.seenu.dev.android.smartstep.home.home_data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.seenu.dev.android.smartstep.home.home_data.sensor.StepSensorController
import com.seenu.dev.android.smartstep.home.home_domain.PreferenceManager
import com.seenu.dev.android.smartstep.home.home_domain.StepRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class StepTrackingService : Service() {

    private val stepRepository: StepRepository by inject()
    private val preferenceManager: PreferenceManager by inject()

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var sensorController: StepSensorController? = null

    override fun onCreate() {
        super.onCreate()

        sensorController = StepSensorController(this) { activeSecondsDelta ->
            serviceScope.launch {
                stepRepository.addStep(activeSecondsDelta)
            }
        }

        observePauseState()
    }

    private fun observePauseState() {
        serviceScope.launch {
            preferenceManager.isStepTrackingPaused.collect { isPaused ->
                if (isPaused) sensorController?.stopListening()
                else sensorController?.startListening()
            }
        }
    }

    // TODO: Start Foreground Service Notification here so the OS doesn't kill it
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // You MUST create a NotificationChannel and a Notification here
        // val notification = NotificationCompat.Builder(this, "step_channel")...
        // startForeground(1, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorController?.stopListening()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}