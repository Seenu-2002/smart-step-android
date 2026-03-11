package com.seenu.dev.android.smartstep.home.home_presentation.service

import android.Manifest
import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.PermissionChecker
import com.seenu.dev.android.smartstep.domain.repository.UserConfigRepository
import com.seenu.dev.android.smartstep.home.home_domain.StepSensorRepository
import com.seenu.dev.android.smartstep.home.home_presentation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SmartStepNotificationService : Service() {
    private val stepSensorRepository: StepSensorRepository by inject()
    private val userConfigRepository: UserConfigRepository by inject()

    private lateinit var expandedView: RemoteViews
    private lateinit var collapsedViews: RemoteViews
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var observationJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
        prepareNotification()

        serviceScope.launch {
            stepSensorRepository.startCountingSteps()
        }

        observationJob?.cancel()
        observationJob = combine(
            stepSensorRepository.observeTodaySteps(),
            userConfigRepository.getUserConfigFlow().map { it.targetStepCount }
        ) { steps, targetStepCount ->
            updateNotification(steps, (steps * 100) / targetStepCount)
        }.launchIn(serviceScope)
    }

    private fun prepareNotification() {
        collapsedViews = RemoteViews(
            packageName,
            R.layout.smart_step_collapsed_notification
        )
        expandedView = RemoteViews(
            packageName,
            R.layout.smart_step_expanded_notification
        )

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_steps)
            .setOngoing(true)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(collapsedViews)
            .setCustomBigContentView(expandedView)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return START_STICKY
    }

    private fun startForeground() {
        Log.d("asd", "HomeScreenRoot: startForeground")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val permission =
                PermissionChecker.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION
                )

            if (permission != PermissionChecker.PERMISSION_GRANTED) {
                stopSelf()
                return
            }
        }

        try {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                createNotification(0),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
                } else {
                    0
                },
            )
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                // App not in a valid state to start foreground service (e.g. started from bg)
                return
            }
        }
        Log.d("asd", "HomeScreenRoot: end")
    }

    override fun onBind(p0: Intent?) = null

    private fun createNotification(steps: Int): Notification {


        return notificationBuilder.build()
    }

    private fun updateNotification(steps: Int, progress: Int) {
        // 3. Update ONLY the specific TextView inside the layout
        expandedView.setTextViewText(R.id.txt_vw_steps, steps.toString())
        collapsedViews.setTextViewText(R.id.txt_vw_steps, steps.toString())

        expandedView.setProgressBar(R.id.progress_steps, 100, progress, false)
        collapsedViews.setProgressBar(R.id.progress_steps, 100, progress, false)



        // 4. Notify using the SAME builder instance
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Step Counter",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        stepSensorRepository.stopCountingSteps()
        serviceScope.cancel()
        observationJob = null
        super.onDestroy()
    }

    companion object {
        const val SMART_STEP_NOTI_DATA = "SMART_STEP_NOTI_DATA"
        const val STEPS_KEY = "STEPS_KEY"
        const val CALORIES_KEY = "CALORIES_KEY"
        const val PROGRESS_KEY = "PROGRESS_KEY"
        private const val CHANNEL_ID = "step_counter_channel"
        private const val NOTIFICATION_ID = 1
    }
}