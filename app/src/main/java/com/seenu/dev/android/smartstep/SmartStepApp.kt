package com.seenu.dev.android.smartstep

import android.app.Application
import com.seenu.dev.android.smartstep.data.di.coreDataModule
import com.seenu.dev.android.smartstep.di.appModule
import com.seenu.dev.android.smartstep.home.home_data.di.homeDataModule
import com.seenu.dev.android.smartstep.home.home_presentation.di.homePresentationModule
import com.seenu.dev.android.smartstep.profile_setup.di.profileSetupModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SmartStepApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin errors/info
            androidLogger()
            // Reference Android context
            androidContext(this@SmartStepApp)
            // Load modules
            modules(
                appModule,
                homePresentationModule,
                homeDataModule,
                coreDataModule,
                profileSetupModule,
            )
        }
    }
}