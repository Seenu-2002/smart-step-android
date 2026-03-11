package com.seenu.dev.android.smartstep.home.home_data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.seenu.dev.android.smartstep.home.home_data.BatteryOptimizationRepositoryImpl
import com.seenu.dev.android.smartstep.home.home_data.PreferenceManagerImpl
import com.seenu.dev.android.smartstep.home.home_data.StepSensorPreferencesImpl
import com.seenu.dev.android.smartstep.home.home_data.StepSensorRepositoryImpl
import com.seenu.dev.android.smartstep.home.home_data.dataStore
import com.seenu.dev.android.smartstep.home.home_data.datasource.StepSensorDataSource
import com.seenu.dev.android.smartstep.home.home_data.local.database.StepsDatabase
import com.seenu.dev.android.smartstep.home.home_data.stepSensorDataStore
import com.seenu.dev.android.smartstep.home.home_domain.BatteryOptimizationRepository
import com.seenu.dev.android.smartstep.home.home_domain.PreferenceManager
import com.seenu.dev.android.smartstep.home.home_domain.StepSensorPreferences
import com.seenu.dev.android.smartstep.home.home_domain.StepSensorRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.jvm.java

val homeDataModule = module {
    single<DataStore<Preferences>> { androidContext().dataStore }

    single<PreferenceManager> { PreferenceManagerImpl(get()) }

    single<BatteryOptimizationRepository> { BatteryOptimizationRepositoryImpl(androidContext()) }

    // 1. Provide the Database and DAO
    single {
        Room
            .databaseBuilder(
                androidContext(),
                StepsDatabase::class.java,
                "steps-db",
            )
            .build()
    }
    single { get<StepsDatabase>().dailyStepsDao }

    // 2. Provide DataSources and Preferences
    // 'get()' tells Koin to find the Context or other dependencies automatically
    single { StepSensorDataSource(get()) }

    single<DataStore<Preferences>> { androidContext().stepSensorDataStore }

    single<StepSensorPreferences> {
        StepSensorPreferencesImpl(get())
    }

    single<StepSensorRepository> {
        StepSensorRepositoryImpl(
            stepSensorDataSource = get(),
            stepsDao = get(),
            stepSensorPreferences = get()
        )
    }
}