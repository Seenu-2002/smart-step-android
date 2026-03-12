package com.seenu.dev.android.smartstep.home.home_presentation.service

import android.Manifest
import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.PermissionChecker
import com.seenu.dev.android.smartstep.domain.model.Gender
import com.seenu.dev.android.smartstep.domain.model.NavigationConstants
import com.seenu.dev.android.smartstep.domain.model.WeightMetric
import com.seenu.dev.android.smartstep.domain.repository.UserConfigRepository
import com.seenu.dev.android.smartstep.home.home_domain.PreferenceManager
import com.seenu.dev.android.smartstep.home.home_domain.StepMetricsCalculator
import com.seenu.dev.android.smartstep.home.home_domain.StepRepository
import com.seenu.dev.android.smartstep.home.home_presentation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.jvm.java

class SmartStepNotificationService : Service() {
    private val stepSensorRepository: StepRepository by inject()
    private val userConfigRepository: UserConfigRepository by inject()
    private val preferenceManager: PreferenceManager by inject()

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
            preferenceManager.isStepTrackingPaused.collect { isPaused ->
                if (isPaused) stepSensorRepository.stopCountingSteps()
                else stepSensorRepository.startCountingSteps()
            }
        }

        observationJob?.cancel()
        observationJob = combine(
            stepSensorRepository.getTodaySteps(),
            userConfigRepository.getUserConfigFlow()
        ) { steps, userConfig ->
            val weightValue = userConfig.weightMetric.getWeightValue().toFloat()
            val calories = StepMetricsCalculator.calculateCalories(
                steps,
                weightValue,
                userConfig.weightMetric is WeightMetric.Pounds,
                userConfig.gender == Gender.MALE
            )
            updateNotification(
                steps = steps,
                progress = ((steps * 100) / userConfig.targetStepCount),
                calories = calories
            )
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
            .setContentIntent(getMainActivityPendingIntent())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return START_STICKY
    }

    private fun startForeground() {
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
                createNotification(),
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
    }

    override fun onBind(p0: Intent?) = null

    private fun createNotification(): Notification {
        return notificationBuilder.build()
    }

    private fun updateNotification(
        steps: Int,
        progress: Int,
        calories: Int
    ) {
        expandedView.setTextViewText(R.id.txt_vw_steps, steps.toString())
        collapsedViews.setTextViewText(R.id.txt_vw_steps, steps.toString())

        expandedView.setTextViewText(R.id.txt_vw_weight, calories.toString())
        collapsedViews.setTextViewText(R.id.txt_vw_weight, calories.toString())

        expandedView.setProgressBar(R.id.progress_steps, 100, progress, false)
        collapsedViews.setProgressBar(R.id.progress_steps, 100, progress, false)

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

    private fun getMainActivityPendingIntent(): PendingIntent {
        val intent = Intent(NavigationConstants.MAIN_ACTIVITY_ACTION).apply {
            // Optional: ensure only your app receives this
            `package` = this.`package`
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        return PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onDestroy() {
        stepSensorRepository.stopCountingSteps()
        serviceScope.cancel()
        observationJob = null
        super.onDestroy()
    }

    companion object {
        private const val CHANNEL_ID = "step_counter_channel"
        private const val NOTIFICATION_ID = 1
    }
}