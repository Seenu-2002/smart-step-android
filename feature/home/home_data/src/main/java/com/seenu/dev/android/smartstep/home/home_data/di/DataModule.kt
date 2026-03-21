package com.seenu.dev.android.smartstep.home.home_data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.seenu.dev.android.smartstep.home.home_data.BatteryOptimizationRepositoryImpl
import com.seenu.dev.android.smartstep.home.home_data.PreferenceManagerImpl
import com.seenu.dev.android.smartstep.home.home_data.StepRepositoryImpl
import com.seenu.dev.android.smartstep.home.home_data.StepSensorPreferencesImpl
import com.seenu.dev.android.smartstep.home.home_data.dataStore
import com.seenu.dev.android.smartstep.home.home_data.local.StepDatabase
import com.seenu.dev.android.smartstep.home.home_data.sensor.StepSensorDataSource
import com.seenu.dev.android.smartstep.home.home_data.stepSensorDataStore
import com.seenu.dev.android.smartstep.home.home_domain.BatteryOptimizationRepository
import com.seenu.dev.android.smartstep.home.home_domain.PreferenceManager
import com.seenu.dev.android.smartstep.home.home_domain.StepRepository
import com.seenu.dev.android.smartstep.home.home_domain.StepSensorPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeDataModule = module {
    single<DataStore<Preferences>>(named("dataStore")) { androidContext().dataStore }

    single<PreferenceManager> { PreferenceManagerImpl(get(named("dataStore"))) }

    single<BatteryOptimizationRepository> { BatteryOptimizationRepositoryImpl(androidContext()) }

    single {
        Room.databaseBuilder(
            androidContext(),
            StepDatabase::class.java,
            "smartstep_db"
        ).build()
    }

    single { get<StepDatabase>().stepDao }

    single<StepRepository> { StepRepositoryImpl(
        stepSensorDataSource = get(),
        stepDao = get(),
        stepSensorPreferences = get(),
        userConfigRepository = get()
    ) }

    single { StepSensorDataSource(get()) }

    single<DataStore<Preferences>>(named("stepSensorDataStore")) { androidContext().stepSensorDataStore }

    single<StepSensorPreferences> {
        StepSensorPreferencesImpl(get(named("stepSensorDataStore")))
    }


}